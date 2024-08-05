package az.example.eventsapp.mapper;

import az.example.eventsapp.entity.VenueEntity;
import az.example.eventsapp.request.VenueRequest;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-01T21:03:13+0400",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.8.jar, environment: Java 17.0.8 (Oracle Corporation)"
)
@Component
public class VenueMapperImpl implements VenueMapper {

    @Override
    public VenueEntity toVenueEntity(VenueRequest venueRequest) {
        if ( venueRequest == null ) {
            return null;
        }

        VenueEntity venueEntity = new VenueEntity();

        venueEntity.setName( venueRequest.getName() );
        venueEntity.setAddress( venueRequest.getAddress() );
        if ( venueRequest.getCapacity() != null ) {
            venueEntity.setCapacity( venueRequest.getCapacity() );
        }

        return venueEntity;
    }
}
