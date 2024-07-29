package az.example.eventsapp.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventCardResponse {

    private Long id;
    private String name;
    private LocalDateTime startDate;
    private Double price;
    private String imageUrl;

}
