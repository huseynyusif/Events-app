package az.example.eventsapp.request;

import az.example.eventsapp.entity.*;
import az.example.eventsapp.enums.EventStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long categoryId;
    private VenueRequest venue;
    private List<TicketRequest> tickets;
}
