package az.example.eventsapp.service;

import az.example.eventsapp.entity.EventEntity;
import az.example.eventsapp.entity.UserEntity;
import az.example.eventsapp.exception.EventNotFoundException;
import az.example.eventsapp.exception.EventNotInFavoriteList;
import az.example.eventsapp.exception.UserNotFoundException;
import az.example.eventsapp.repository.EventRepository;
import az.example.eventsapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserFavoriteService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public void addFavoriteEvent(Long eventId, String username){
        log.info("Adding event with ID: {} to favorites for user: {}", eventId, username);
        UserEntity  user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found with username: {}", username);
                    return new UserNotFoundException();
                });
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    log.error("Event not found with ID: {}", eventId);
                    return new EventNotFoundException();
                });

        user.getFavoriteEvents().add(event);
        event.getFavoritedBy().add(user);

        userRepository.save(user);
        eventRepository.save(event);
        log.info("Event with ID: {} successfully added to favorites for user: {}", eventId, username);
    }

    public void removeFavoriteEvent(Long eventId, String username) {
        log.info("Removing event with ID: {} from favorites for user: {}", eventId, username);
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found with username: {}", username);
                    return new UserNotFoundException();
                });
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    log.error("Event not found with ID: {}", eventId);
                    return new EventNotFoundException();
                });

        if (!user.getFavoriteEvents().contains(event)) {
            log.warn("Event with ID: {} is not in the favorite list for user: {}", eventId, username);
            throw new EventNotInFavoriteList();
        }

        user.getFavoriteEvents().remove(event);
        event.getFavoritedBy().remove(user);

        userRepository.save(user);
        eventRepository.save(event);
        log.info("Event with ID: {} successfully removed from favorites for user: {}", eventId, username);
    }

    public List<EventEntity> getFavoriteEvents(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found with username: {}", username);
                    return new UserNotFoundException();
                });
        List<EventEntity> favoriteEvents = new ArrayList<>(user.getFavoriteEvents());
        log.info("Retrieved {} favorite events for user: {}", favoriteEvents.size(), username);
        return favoriteEvents;
    }
}
