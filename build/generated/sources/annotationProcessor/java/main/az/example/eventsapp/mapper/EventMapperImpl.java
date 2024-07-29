package az.example.eventsapp.mapper;

import az.example.eventsapp.entity.CategoryEntity;
import az.example.eventsapp.entity.EventEntity;
import az.example.eventsapp.entity.ReviewEntity;
import az.example.eventsapp.entity.TicketEntity;
import az.example.eventsapp.entity.UserEntity;
import az.example.eventsapp.entity.VenueEntity;
import az.example.eventsapp.request.EventRequest;
import az.example.eventsapp.request.TicketRequest;
import az.example.eventsapp.request.VenueRequest;
import az.example.eventsapp.response.CategoryResponse;
import az.example.eventsapp.response.EventCardResponse;
import az.example.eventsapp.response.EventResponse;
import az.example.eventsapp.response.TicketResponse;
import az.example.eventsapp.response.UserResponse;
import az.example.eventsapp.response.VenueResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-29T01:52:06+0400",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.8.jar, environment: Java 17.0.8 (Oracle Corporation)"
)
@Component
public class EventMapperImpl implements EventMapper {

    @Override
    public EventResponse toEventResponse(EventEntity event, Set<ReviewEntity> reviews) {
        if ( event == null && reviews == null ) {
            return null;
        }

        EventResponse.EventResponseBuilder eventResponse = EventResponse.builder();

        if ( event != null ) {
            eventResponse.category( categoryEntityToCategoryResponse( event.getCategory() ) );
            eventResponse.images( mapImages( event.getImages() ) );
            eventResponse.id( event.getId() );
            eventResponse.name( event.getName() );
            eventResponse.description( event.getDescription() );
            eventResponse.startDate( event.getStartDate() );
            eventResponse.endDate( event.getEndDate() );
            eventResponse.status( event.getStatus() );
            eventResponse.venue( venueEntityToVenueResponse( event.getVenue() ) );
            eventResponse.organizer( userEntityToUserResponse( event.getOrganizer() ) );
            eventResponse.tickets( ticketEntityListToTicketResponseList( event.getTickets() ) );
        }
        eventResponse.averageRating( calculateAverageRating( reviews ) );

        return eventResponse.build();
    }

    @Override
    public EventEntity toEventEntity(EventRequest eventRequest) {
        if ( eventRequest == null ) {
            return null;
        }

        EventEntity eventEntity = new EventEntity();

        eventEntity.setName( eventRequest.getName() );
        eventEntity.setDescription( eventRequest.getDescription() );
        eventEntity.setStartDate( eventRequest.getStartDate() );
        eventEntity.setEndDate( eventRequest.getEndDate() );
        eventEntity.setVenue( venueRequestToVenueEntity( eventRequest.getVenue() ) );
        eventEntity.setTickets( ticketRequestListToTicketEntityList( eventRequest.getTickets() ) );

        return eventEntity;
    }

    @Override
    public EventCardResponse toEventCardResponse(EventEntity event) {
        if ( event == null ) {
            return null;
        }

        EventCardResponse.EventCardResponseBuilder eventCardResponse = EventCardResponse.builder();

        eventCardResponse.price( mapPrice( event.getTickets() ) );
        eventCardResponse.imageUrl( mapImageUrl( event.getImages() ) );
        eventCardResponse.id( event.getId() );
        eventCardResponse.name( event.getName() );
        eventCardResponse.startDate( event.getStartDate() );

        return eventCardResponse.build();
    }

    protected CategoryResponse categoryEntityToCategoryResponse(CategoryEntity categoryEntity) {
        if ( categoryEntity == null ) {
            return null;
        }

        CategoryResponse.CategoryResponseBuilder categoryResponse = CategoryResponse.builder();

        categoryResponse.name( mapToString( categoryEntity.getName() ) );
        categoryResponse.description( mapToString( categoryEntity.getDescription() ) );
        categoryResponse.id( categoryEntity.getId() );

        return categoryResponse.build();
    }

    protected VenueResponse venueEntityToVenueResponse(VenueEntity venueEntity) {
        if ( venueEntity == null ) {
            return null;
        }

        VenueResponse.VenueResponseBuilder venueResponse = VenueResponse.builder();

        venueResponse.id( venueEntity.getId() );
        if ( venueEntity.getName() != null ) {
            venueResponse.name( venueEntity.getName().name() );
        }
        venueResponse.address( venueEntity.getAddress() );
        venueResponse.capacity( venueEntity.getCapacity() );

        return venueResponse.build();
    }

    protected UserResponse userEntityToUserResponse(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.id( userEntity.getId() );
        userResponse.name( userEntity.getName() );
        userResponse.surname( userEntity.getSurname() );
        userResponse.telephoneNumber( userEntity.getTelephoneNumber() );
        userResponse.email( userEntity.getEmail() );
        userResponse.username( userEntity.getUsername() );

        return userResponse.build();
    }

    protected TicketResponse ticketEntityToTicketResponse(TicketEntity ticketEntity) {
        if ( ticketEntity == null ) {
            return null;
        }

        TicketResponse.TicketResponseBuilder ticketResponse = TicketResponse.builder();

        ticketResponse.id( ticketEntity.getId() );
        ticketResponse.quantity( ticketEntity.getQuantity() );
        ticketResponse.type( ticketEntity.getType() );
        ticketResponse.price( ticketEntity.getPrice() );

        return ticketResponse.build();
    }

    protected List<TicketResponse> ticketEntityListToTicketResponseList(List<TicketEntity> list) {
        if ( list == null ) {
            return null;
        }

        List<TicketResponse> list1 = new ArrayList<TicketResponse>( list.size() );
        for ( TicketEntity ticketEntity : list ) {
            list1.add( ticketEntityToTicketResponse( ticketEntity ) );
        }

        return list1;
    }

    protected VenueEntity venueRequestToVenueEntity(VenueRequest venueRequest) {
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

    protected TicketEntity ticketRequestToTicketEntity(TicketRequest ticketRequest) {
        if ( ticketRequest == null ) {
            return null;
        }

        TicketEntity ticketEntity = new TicketEntity();

        if ( ticketRequest.getQuantity() != null ) {
            ticketEntity.setQuantity( ticketRequest.getQuantity() );
        }
        ticketEntity.setType( ticketRequest.getType() );
        ticketEntity.setPrice( ticketRequest.getPrice() );

        return ticketEntity;
    }

    protected List<TicketEntity> ticketRequestListToTicketEntityList(List<TicketRequest> list) {
        if ( list == null ) {
            return null;
        }

        List<TicketEntity> list1 = new ArrayList<TicketEntity>( list.size() );
        for ( TicketRequest ticketRequest : list ) {
            list1.add( ticketRequestToTicketEntity( ticketRequest ) );
        }

        return list1;
    }
}
