package az.example.eventsapp.response;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class EventCriteria {
    private String name;
    private LocalDateTime startDateFrom;
    private LocalDateTime startDateTo;
    private Double minPrice;
    private Double maxPrice;
    private String venueName;
    private String sortBy;
}
