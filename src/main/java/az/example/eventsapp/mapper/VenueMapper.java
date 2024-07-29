package az.example.eventsapp.mapper;

import az.example.eventsapp.entity.VenueEntity;
import az.example.eventsapp.response.VenueResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface VenueMapper {
    VenueResponse toVenueResponse(VenueEntity venue);

    VenueEntity toVenueEntity(VenueResponse venueResponse);
}
