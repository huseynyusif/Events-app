package az.example.eventsapp.controller;

import az.example.eventsapp.exception.UnauthorizedException;
import az.example.eventsapp.request.EventRequest;
import az.example.eventsapp.response.EventCardResponse;
import az.example.eventsapp.response.EventCriteria;
import az.example.eventsapp.response.EventResponse;
import az.example.eventsapp.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/hello")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Firuz");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponse createEvent(@RequestPart EventRequest eventRequest,
                                                     @RequestPart(value = "mainPicture") MultipartFile mainPicture,
                                                     @RequestPart(value = "image1", required = false) MultipartFile image1,
                                                     @RequestPart(value = "image2", required = false) MultipartFile image2,
                                                     @RequestPart(value = "image3", required = false) MultipartFile image3,
                                                     @RequestPart(value = "image4", required = false) MultipartFile image4,
                                                     Authentication authentication) {
        if (authentication == null) {
            throw new UnauthorizedException();
        }

        List<MultipartFile> images = collectImages(mainPicture, image1, image2, image3, image4);

        return eventService.createEvent(eventRequest, authentication.getName(), images);
    }

    @PostMapping("/{eventId}/buy-tickets")
    public List<String> purchaseTicket(
            @PathVariable Long eventId,
            @RequestParam String type,
            @RequestParam int quantityToPurchase,
            Authentication authentication) {

        if (authentication == null) {
            throw new UnauthorizedException();
        }

        return eventService.purchaseTicket(eventId, type, quantityToPurchase, authentication.getName());
    }


    @GetMapping("/cards")
    public List<EventCardResponse> getEventCards() {
        return eventService.getEventCards();
    }

    @GetMapping("/{id}")
    public EventResponse getEventDetails(@PathVariable Long id) {
        EventResponse eventResponse = eventService.getEventDetails(id);
        return eventResponse;
    }

    @GetMapping("/page/filter")
    public Page<EventCardResponse> filterEvents(
            //extra all event lazim deyil
            @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable,
            @ModelAttribute EventCriteria eventCriteria) {
        return eventService.filterEvents(pageable, eventCriteria);
    }

    @GetMapping("/sorted-by-star-rating")
    public List<EventCardResponse> getEventsSortedByStarRating() {
        return eventService.getEventsSortedByStarRating();
    }

    @GetMapping("/latest")
    public List<EventCardResponse> getLatestEvents() {
        return eventService.getLatestEvents();
    }

    @GetMapping("/category/{categoryId}")
    public List<EventCardResponse> gtEventsByCategory(@PathVariable Long categoryId) {
        return eventService.getEventsByCategory(categoryId);
    }

    @GetMapping("/search")
    public List<EventCardResponse> searchEvents(@RequestParam String name) {
        return eventService.searchEventsByName(name);
    }

    @GetMapping("/autocomplete")
    public List<String> autocompleteEventNames(@RequestParam String query) {
         return eventService.getAutocompleteEventNames(query);
    }

    @PutMapping("/update/{eventId}")
    public EventResponse updateEvent(@PathVariable Long eventId,
                                                     @RequestPart(value = "event") EventRequest eventRequest,
                                                     @RequestPart(value = "mainPicture") MultipartFile mainPicture,
                                                     @RequestPart(value = "image1", required = false) MultipartFile image1,
                                                     @RequestPart(value = "image2", required = false) MultipartFile image2,
                                                     @RequestPart(value = "image3", required = false) MultipartFile image3,
                                                     @RequestPart(value = "image4", required = false) MultipartFile image4,
                                                     Authentication authentication) {
        if (authentication == null) {
            throw new UnauthorizedException();
        }

        List<MultipartFile> images = collectImages(mainPicture, image1, image2, image3, image4);
        return eventService.updateEvent(eventId, eventRequest, authentication.getName(), images);
    }

    private static List<MultipartFile> collectImages(MultipartFile mainPicture, MultipartFile... otherImages) {
        List<MultipartFile> images = new ArrayList<>();
        if (mainPicture != null) {
            images.add(mainPicture);
        }
        for (MultipartFile image : otherImages) {
            if (image != null) {
                images.add(image);
            }
        }
        return images;
    }

    @GetMapping("/organizer/{username}")
    public List<EventCardResponse> getEventsByOrganizer(@PathVariable String username) {
        return eventService.getEventsByOrganizer(username);
    }

    @GetMapping("/my-events")
    public List<EventCardResponse> getMyEvents(@AuthenticationPrincipal UserDetails authentication) {
        return eventService.getEventsByOrganizer(authentication.getUsername());
    }

}
