package az.example.eventsapp.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class EventCriteria {
    private String name;
    private LocalDateTime startDateFrom;
    private LocalDateTime startDateTo;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String venueName;
    private String sortBy;
}
