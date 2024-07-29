package az.example.eventsapp.service;

import az.example.eventsapp.entity.EventEntity;
import az.example.eventsapp.entity.UserEntity;
import az.example.eventsapp.exception.EventNotFoundException;
import az.example.eventsapp.exception.EventNotInFavoriteList;
import az.example.eventsapp.exception.UserNotFoundException;
import az.example.eventsapp.repository.EventRepository;
import az.example.eventsapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserFavoriteService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public void addFavoriteEvent(Long eventId, String username){
        UserEntity  user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(EventNotFoundException::new);

        user.getFavoriteEvents().add(event);
        event.getFavoritedBy().add(user);

        userRepository.save(user);
        eventRepository.save(event);
    }

    public void removeFavoriteEvent(Long eventId, String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(EventNotFoundException::new);

        if (!user.getFavoriteEvents().contains(event)) {
            throw new EventNotInFavoriteList();
        }

        user.getFavoriteEvents().remove(event);
        event.getFavoritedBy().remove(user);

        userRepository.save(user);
        eventRepository.save(event);
    }

    public List<EventEntity> getFavoriteEvents(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        return new ArrayList<>(user.getFavoriteEvents());
    }
}
