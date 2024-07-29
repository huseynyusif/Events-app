package az.example.eventsapp.mapper;

import az.example.eventsapp.entity.VenueEntity;
import az.example.eventsapp.enums.City;
import az.example.eventsapp.response.VenueResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-29T01:52:06+0400",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.8.jar, environment: Java 17.0.8 (Oracle Corporation)"
)
@Component
public class VenueMapperImpl implements VenueMapper {

    @Override
    public VenueResponse toVenueResponse(VenueEntity venue) {
        if ( venue == null ) {
            return null;
        }

        VenueResponse.VenueResponseBuilder venueResponse = VenueResponse.builder();

        venueResponse.id( venue.getId() );
        if ( venue.getName() != null ) {
            venueResponse.name( venue.getName().name() );
        }
        venueResponse.address( venue.getAddress() );
        venueResponse.capacity( venue.getCapacity() );

        return venueResponse.build();
    }

    @Override
    public VenueEntity toVenueEntity(VenueResponse venueResponse) {
        if ( venueResponse == null ) {
            return null;
        }

        VenueEntity venueEntity = new VenueEntity();

        venueEntity.setId( venueResponse.getId() );
        if ( venueResponse.getName() != null ) {
            venueEntity.setName( Enum.valueOf( City.class, venueResponse.getName() ) );
        }
        venueEntity.setAddress( venueResponse.getAddress() );
        venueEntity.setCapacity( venueResponse.getCapacity() );

        return venueEntity;
    }
}
