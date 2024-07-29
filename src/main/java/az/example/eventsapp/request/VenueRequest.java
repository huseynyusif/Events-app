package az.example.eventsapp.request;

import az.example.eventsapp.enums.City;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VenueRequest {
    private City name;
    private String address;
    private Integer capacity;
}
