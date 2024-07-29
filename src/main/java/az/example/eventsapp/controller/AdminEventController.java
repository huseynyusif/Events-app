package az.example.eventsapp.controller;

import az.example.eventsapp.response.EventResponse;
import az.example.eventsapp.service.AdminEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final AdminEventService adminEventService;

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{eventId}/approve")
    public EventResponse approveEvent(@PathVariable Long eventId) {
        return adminEventService.approveEvent(eventId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("all-events")
    public List<EventResponse> getAllEventsForAdmin() {
        return adminEventService.getAllEventsForAdmin();
    }

    @PutMapping("/deactivate/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public EventResponse deactivateEvent(@PathVariable Long eventId) {
        return adminEventService.deactivateEvent(eventId);
    }
}
