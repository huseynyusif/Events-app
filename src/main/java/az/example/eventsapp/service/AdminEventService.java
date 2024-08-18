package az.example.eventsapp.service;

import az.example.eventsapp.entity.EventEntity;
import az.example.eventsapp.entity.ImageEntity;
import az.example.eventsapp.enums.EventStatus;
import az.example.eventsapp.exception.EventNotFoundException;
import az.example.eventsapp.mapper.EventMapper;
import az.example.eventsapp.repository.EventRepository;
import az.example.eventsapp.repository.ReviewRepository;
import az.example.eventsapp.response.EventResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminEventService {

    private final EventRepository eventRepository;
    private final ReviewRepository reviewRepository;
    private final EventMapper eventMapper;


    public EventResponse approveEvent(Long eventId) {
        log.info("Approving event with ID: {}", eventId);

        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    log.error("Event not found with ID: {}", eventId);
                    return new EventNotFoundException();
                });

        event.setStatus(EventStatus.ACTIVE);
        EventEntity updatedEvent = eventRepository.save(event);

        log.info("Event with ID: {} approved and activated", eventId);
        return eventMapper.toEventResponse(updatedEvent,null);
    }


    public List<EventResponse> getAllEventsForAdmin() {
        log.info("Fetching all events for admin");

        List<EventEntity> eventEntities = eventRepository.findAll();
        List<EventResponse> eventResponses = eventEntities.stream()
                .map(eventEntity -> {
                    log.info("Processing event with ID: {}", eventEntity.getId());
                    var reviews = reviewRepository.findByEventId(eventEntity.getId());
                    List<String> imageUrls = eventEntity.getImages().stream()
                            .map(ImageEntity::getUrl)
                            .toList();
                    EventResponse eventResponse = eventMapper.toEventResponse(eventEntity, reviews);
                    eventResponse.setImages(imageUrls);
                    return eventResponse;
                }).toList();

        log.info("Fetched {} events for admin", eventResponses.size());
        return eventResponses;
    }

    public EventResponse deactivateEvent(Long eventId) {
        log.info("Deactivating event with ID: {}", eventId);

        EventEntity eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    log.error("Event not found with ID: {}", eventId);
                    return new EventNotFoundException();
                });

        eventEntity.setStatus(EventStatus.INACTIVE);
        eventRepository.save(eventEntity);

        log.info("Event with ID: {} deactivated", eventId);
        return eventMapper.toEventResponse(eventEntity,null);
    }
}
