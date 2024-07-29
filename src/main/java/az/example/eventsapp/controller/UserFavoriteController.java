package az.example.eventsapp.controller;

import az.example.eventsapp.entity.EventEntity;
import az.example.eventsapp.mapper.EventMapper;
import az.example.eventsapp.response.EventResponse;
import az.example.eventsapp.service.UserFavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class UserFavoriteController {

    private final UserFavoriteService userFavoriteService;
    private final EventMapper eventMapper;

    @PostMapping("/add/{eventId}")
    public String addFavorite(@PathVariable Long eventId, Authentication authentication){
        userFavoriteService.addFavoriteEvent(eventId,authentication.getName());
        return "Event added to favorites";
    }

    @PostMapping("/remove/{eventId}")
    public String removeFavorite(@PathVariable Long eventId, Authentication authentication) {
        userFavoriteService.removeFavoriteEvent(eventId, authentication.getName());
        return "Event removed from favorites";
    }

    @GetMapping("/list")
    public List<EventResponse> getFavoriteEvents(Authentication authentication) {
        List<EventEntity> favoriteEvents = userFavoriteService.getFavoriteEvents(authentication.getName());
        return favoriteEvents.stream()
                .map(event -> eventMapper.toEventResponse(event, event.getOrganizer().getReviews()))
                .collect(Collectors.toList());
    }
}
