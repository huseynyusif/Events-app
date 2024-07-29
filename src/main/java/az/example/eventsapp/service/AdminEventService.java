package az.example.eventsapp.service;

import az.example.eventsapp.entity.EventEntity;
import az.example.eventsapp.entity.ImageEntity;
import az.example.eventsapp.entity.ReviewEntity;
import az.example.eventsapp.enums.EventStatus;
import az.example.eventsapp.exception.EventNotFoundException;
import az.example.eventsapp.mapper.EventMapper;
import az.example.eventsapp.repository.EventRepository;
import az.example.eventsapp.repository.ReviewRepository;
import az.example.eventsapp.response.EventResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminEventService {

    private final EventRepository eventRepository;
    private final ReviewRepository reviewRepository;
    private final EventMapper eventMapper;


    public EventResponse approveEvent(Long eventId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(EventNotFoundException::new);

        event.setStatus(EventStatus.ACTIVE);
        EventEntity updatedEvent = eventRepository.save(event);

        return eventMapper.toEventResponse(updatedEvent,null);
    }


    public List<EventResponse> getAllEventsForAdmin() {
        List<EventEntity> eventEntities = eventRepository.findAll();
        return eventEntities.stream()
                .map(eventEntity -> {
                    var reviews = reviewRepository.findByEventId(eventEntity.getId());
                    List<String> imageUrls = eventEntity.getImages().stream()
                            .map(ImageEntity::getUrl)
                            .toList();
                    EventResponse eventResponse = eventMapper.toEventResponse(eventEntity, reviews);
                    eventResponse.setImages(imageUrls);
                    return eventResponse;
                }).toList();
    }

    public EventResponse deactivateEvent(Long eventId) {
        EventEntity eventEntity = eventRepository.findById(eventId)
                .orElseThrow(EventNotFoundException::new);

        eventEntity.setStatus(EventStatus.INACTIVE);
        eventRepository.save(eventEntity);

        return eventMapper.toEventResponse(eventEntity,null);
    }
}
