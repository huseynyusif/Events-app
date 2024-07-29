package az.example.eventsapp.service;

import az.example.eventsapp.entity.*;
import az.example.eventsapp.enums.EventStatus;
import az.example.eventsapp.exception.*;
import az.example.eventsapp.mapper.EventMapper;
import az.example.eventsapp.repository.*;
import az.example.eventsapp.request.EventRequest;
import az.example.eventsapp.request.TicketRequest;
import az.example.eventsapp.response.EventCardResponse;
import az.example.eventsapp.response.EventCriteria;
import az.example.eventsapp.response.EventResponse;
import az.example.eventsapp.specification.EventSpecification;
import az.example.eventsapp.util.EmailHelper;
import az.example.eventsapp.util.ImageUploadService;
import az.example.eventsapp.util.PdfService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final VenueRepository venueRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    private final TicketRepository ticketRepository;
    private final UserTicketRepository userTicketRepository;
    private final EmailHelper emailService;
    private final PdfService pdfService;
    private final ImageRepository imageRepository;
    private final ImageUploadService imageUploadService;
    private final ReviewRepository reviewRepository;



    public EventResponse createEvent(EventRequest eventRequest, String username, List<MultipartFile> images) {
        logger.info("Creating event for user: {}", username);

        UserEntity organizer = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        logger.info("Found organizer: {}", organizer.getId());

        CategoryEntity category = categoryRepository.findById(eventRequest.getCategoryId())
                .orElseThrow(CategoryNotFoundException::new);

        logger.info("Found category: {}", category.getId());

        VenueEntity venue = new VenueEntity();
        venue.setName(eventRequest.getVenue().getName());
        venue.setAddress(eventRequest.getVenue().getAddress());
        venue.setCapacity(eventRequest.getVenue().getCapacity());
        venue = venueRepository.save(venue);

        logger.info("Saved venue: {}", venue.getId());

        EventEntity event = new EventEntity();
        event.setCategory(category);
        event.setName(eventRequest.getName());
        event.setDescription(eventRequest.getDescription());
        event.setStartDate(eventRequest.getStartDate());
        event.setEndDate(eventRequest.getEndDate());
        event.setStatus(EventStatus.PENDING);
        event.setVenue(venue);
        event.setOrganizer(organizer);

        List<TicketEntity> tickets = eventRequest.getTickets().stream().map(ticketRequest -> {
            TicketEntity ticket = new TicketEntity();
            ticket.setQuantity(ticketRequest.getQuantity());
            ticket.setType(ticketRequest.getType());
            ticket.setPrice(ticketRequest.getPrice());
            ticket.setEvent(event);
            return ticket;
        }).collect(Collectors.toList());

        event.setTickets(tickets);

        List<ImageEntity> imageEntities = images.stream().map(file -> {
            try {
                String fileName = imageUploadService.uploadFile(file);
                ImageEntity imageEntity = new ImageEntity();
                imageEntity.setUrl(fileName);
                imageEntity.setDescription(file.getOriginalFilename());
                imageEntity.setEvent(event);
                return imageEntity;
            } catch (IOException e) {
                throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
            }
        }).collect(Collectors.toList());

        event.setImages(imageEntities);

        EventEntity savedEvent = eventRepository.save(event);
        logger.info("Saved event: {}", savedEvent.getId());

        ticketRepository.saveAll(tickets);
        logger.info("Saved tickets for event: {}", savedEvent.getId());

        imageRepository.saveAll(imageEntities);
        logger.info("Saved images for event: {}", savedEvent.getId());

        return eventMapper.toEventResponse(savedEvent, null);
    }

    @Transactional
    public List<String> purchaseTicket(Long eventId, String type, int quantityToPurchase, String username) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(EventNotFoundException::new);

        if (!event.isActive()) {
            throw new IllegalArgumentException("Cannot purchase tickets for an inactive event");
        }

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        TicketEntity ticket = event.getTickets().stream()
                .filter(t -> t.getType().equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Ticket type not found for this event"));

        if (ticket.getQuantity() < quantityToPurchase) {
            throw new InsufficientTicketsException();
        }

        List<String> customerCodes = new ArrayList<>();
        List<byte[]> pdfAttachments = new ArrayList<>();
        for (int i = 0; i < quantityToPurchase; i++) {
            String customerCode = generateUniqueCustomerCode();
            customerCodes.add(customerCode);

            UserTicketEntity userTicket = new UserTicketEntity();
            userTicket.setEvent(event);
            userTicket.setUser(user);
            userTicket.setType(ticket.getType());
            userTicket.setPrice(ticket.getPrice());
            userTicket.setCustomerCode(customerCode);
            userTicketRepository.save(userTicket);

            // Create PDF for each ticket
            byte[] pdfAttachment = pdfService.createTicketPdf(event.getName(), user.getName(), ticket.getType(), customerCode);
            pdfAttachments.add(pdfAttachment);
        }

        // Reduce the quantity of tickets
        ticket.setQuantity(ticket.getQuantity() - quantityToPurchase);
        ticketRepository.save(ticket);

        // Send confirmation email
        String subject = "Ticket Purchase Confirmation";
        String body = "Dear " + user.getName() + ",\n\n" +
                "You have successfully purchased " + quantityToPurchase + " ticket(s) for the event " + event.getName() + ".\n" +
                "Your customer codes are: " + String.join(", ", customerCodes) + "\n\n" +
                "Thank you for your purchase!";
        try {
            emailService.sendTicketConfirmationEmail(user.getEmail(), subject, body, pdfAttachments);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return customerCodes;
    }


    private String generateUniqueCustomerCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }


    public List<EventCardResponse> getEventCards() {
        return eventRepository.findAll().stream()
                .filter(event -> event.getStatus() == EventStatus.ACTIVE)
                .map(event -> {
                    EventCardResponse card = new EventCardResponse();
                    card.setId(event.getId());
                    card.setName(event.getName());
                    card.setStartDate(event.getStartDate());
                    // Assuming the event has a list of ticketsAnd we are taking the first one for the price
                    if (!event.getTickets().isEmpty()) {
                        card.setPrice(event.getTickets().get(0).getPrice());
                    }
                    // Set the image URL
                    if (!event.getImages().isEmpty()) {
                        card.setImageUrl(event.getImages().get(0).getUrl());
                    } else {
                        card.setImageUrl("/default/image/path.jpg"); // Default image if no images are available
                    }
                    return card;
                }).collect(Collectors.toList());
    }

    public EventResponse getEventDetails(Long eventId) {
        EventEntity eventEntity = eventRepository.findById(eventId)
                .filter(event -> event.getStatus() == EventStatus.ACTIVE)
                .orElseThrow(EventNotFoundException::new);
        var reviews = reviewRepository.findByEventId(eventId);
        List<String> imageUrls = eventEntity.getImages().stream()
                .map(ImageEntity::getUrl)
                .collect(Collectors.toList());

        EventResponse eventResponse = eventMapper.toEventResponse(eventEntity,reviews);
        eventResponse.setImages(imageUrls);

        return eventResponse;
    }


    public Page<EventCardResponse> filterEvents(Pageable pageable, EventCriteria eventCriteria) {
        Specification<EventEntity> spec = EventSpecification.buildSpecification(
                eventCriteria.getName(),
                eventCriteria.getStartDateFrom(),
                eventCriteria.getStartDateTo(),
                eventCriteria.getMinPrice(),
                eventCriteria.getMaxPrice(),
                eventCriteria.getVenueName()
        );

        // Apply sorting based on criteria
        if ("starRating".equals(eventCriteria.getSortBy())) {
            spec = spec.and(EventSpecification.isSortedByStarRating());
        } else if ("latestEvents".equals(eventCriteria.getSortBy())) {
            spec = spec.and(EventSpecification.isLatestEvents());
        }

        Page<EventEntity> events = eventRepository.findAll(spec, pageable);
        List<EventCardResponse> eventCardResponses = events.stream()
                .map(eventMapper::toEventCardResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(eventCardResponses, pageable, events.getTotalElements());
    }


    public List<EventCardResponse> getEventsSortedByStarRating(){
        List<EventEntity> events = eventRepository.findAll();

        events.sort(Comparator.comparingDouble(event -> calculateStarRatingAverage(event.getId())));
        Collections.reverse(events);

        List<EventCardResponse> eventCards = events.stream()
                .map(event -> {
                    EventCardResponse card = new EventCardResponse();
                    card.setId(event.getId());
                    card.setName(event.getName());
                    card.setStartDate(event.getStartDate());

                    if (!event.getTickets().isEmpty()) {
                        card.setPrice(event.getTickets().get(0).getPrice());
                    }
                    if (!event.getImages().isEmpty()) {
                        card.setImageUrl(event.getImages().get(0).getUrl());
                    } else {
                        card.setImageUrl("/default/image/path.jpg");
                    }
                    return card;
                })
                .toList();

        return eventCards;
    }

    private Double calculateStarRatingAverage(Long eventId) {
        Set<ReviewEntity> reviews = reviewRepository.findByEventId(eventId);
        if (reviews.isEmpty()) {
            return 0.0;
        }

        double sum = reviews.stream()
                .mapToDouble(ReviewEntity::getStarRating)
                .sum();
        return sum / reviews.size();
    }


    public List<EventCardResponse> getLatestEvents() {
        List<EventEntity> latestEvents = eventRepository.findAllByOrderByCreatedDateDesc();
        return latestEvents.stream()
                .map(event -> {
                    EventCardResponse card = new EventCardResponse();
                    card.setId(event.getId());
                    card.setName(event.getName());
                    card.setStartDate(event.getStartDate());
                    // the event has a list of tickets and we are taking the first one for the price
                    if (!event.getTickets().isEmpty()) {
                        card.setPrice(event.getTickets().get(0).getPrice());
                    }
                    // set the image URL
                    if (!event.getImages().isEmpty()) {
                        card.setImageUrl(event.getImages().get(0).getUrl());
                    } else {
                        card.setImageUrl("/default/image/path.jpg"); // Default image if no images are available
                    }
                    return card;
                })
                .collect(Collectors.toList());
    }

    public List<EventCardResponse> getEventsByCategory(Long categoryId) {
        List<EventEntity> events = eventRepository.findByCategoryId(categoryId);
        return events.stream()
                .map(eventMapper::toEventCardResponse)
                .collect(Collectors.toList());
    }

    public List<EventCardResponse> searchEventsByName(String name) {
        List<EventEntity> events = eventRepository.findByNameContainingIgnoreCase(name);
        return events.stream()
                .map(eventMapper::toEventCardResponse)
                .toList();
    }

    public List<String> getAutocompleteEventNames(String query) {
        List<EventEntity> events = eventRepository.findTop10ByNameContainingIgnoreCase(query);
        return events.stream()
                .map(EventEntity::getName)
                .toList();
    }


    public EventResponse updateEvent(Long eventId, EventRequest eventRequest, String username, List<MultipartFile> images) {
        logger.info("Updating event with ID: {} for user: {}", eventId,username);

        UserEntity organizer = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(EventNotFoundException::new);

        if (!event.getOrganizer().equals(organizer)) {
            throw new UnauthorizedActionException();
        }

        if (eventRequest.getCategoryId() != null){
            CategoryEntity category = categoryRepository.findById(eventRequest.getCategoryId())
                    .orElseThrow(CategoryNotFoundException::new);
            event.setCategory(category);
        }

        if (eventRequest.getName() != null){
            event.setName(eventRequest.getName());
        }

        if (eventRequest.getDescription() != null){
            event.setDescription(eventRequest.getDescription());
        }

        if (eventRequest.getStartDate() != null){
            event.setStartDate(eventRequest.getStartDate());
        }

        if (eventRequest.getEndDate() != null){
            event.setEndDate(eventRequest.getEndDate());
        }

        if (eventRequest.getVenue() != null){
            VenueEntity venue = event.getVenue();

            if (eventRequest.getVenue().getName() != null){
                venue.setName(eventRequest.getVenue().getName());
            }
            if (eventRequest.getVenue().getAddress() != null){
                venue.setAddress(eventRequest.getVenue().getAddress());
            }
            if (eventRequest.getVenue().getCapacity() != null){
                venue.setCapacity(eventRequest.getVenue().getCapacity());
            }
            venueRepository.save(venue);
        }

        if (eventRequest.getTickets() != null) {
            for (TicketRequest ticketRequest : eventRequest.getTickets()) {
                TicketEntity ticket = event.getTickets().stream()
                        .filter(t -> t.getType().equals(ticketRequest.getType()))
                        .findFirst()
                        .orElse(null);

                if (ticket == null) {
                    TicketEntity newTicket = new TicketEntity();
                    newTicket.setQuantity(ticketRequest.getQuantity());
                    newTicket.setType(ticketRequest.getType());
                    newTicket.setPrice(ticketRequest.getPrice());
                    newTicket.setEvent(event);
                    ticketRepository.save(newTicket);
                } else {
                    if (ticketRequest.getQuantity() != null) {
                        ticket.setQuantity(ticketRequest.getQuantity());
                    }
                    if (ticketRequest.getPrice() != null) {
                        ticket.setPrice(ticketRequest.getPrice());
                    }
                    ticketRepository.save(ticket);
                }
            }
        }

        if (images != null && !images.isEmpty()) {
            List<ImageEntity> imageEntities = images.stream().map(file -> {
                try {
                    String fileName = imageUploadService.uploadFile(file);
                    ImageEntity imageEntity = new ImageEntity();
                    imageEntity.setUrl(fileName);
                    imageEntity.setDescription(file.getOriginalFilename());
                    imageEntity.setEvent(event);
                    return imageEntity;
                } catch (IOException e) {
                    throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
                }
            }).collect(Collectors.toList());

            imageRepository.saveAll(imageEntities);
            event.setImages(imageEntities);
        }

        EventEntity updatedEvent = eventRepository.save(event);
        logger.info("Updated event: {}", updatedEvent.getId());

        return eventMapper.toEventResponse(updatedEvent, null);
    }

    public List<EventCardResponse> getEventsByOrganizer(String username) {
        List<EventEntity> events = eventRepository.findByOrganizerUsername(username);
        return events.stream()
                .map(eventMapper::toEventCardResponse)
                .toList();
    }
}