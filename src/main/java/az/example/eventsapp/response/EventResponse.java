package az.example.eventsapp.response;

import az.example.eventsapp.entity.CategoryEntity;
import az.example.eventsapp.entity.UserEntity;
import az.example.eventsapp.entity.VenueEntity;
import az.example.eventsapp.enums.EventStatus;
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
public class EventResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private EventStatus status;
    private CategoryResponse category;
    private VenueResponse venue;
    private UserResponse organizer;
    private List<TicketResponse> tickets;
    private List<String> images;
    private Double averageRating;
}