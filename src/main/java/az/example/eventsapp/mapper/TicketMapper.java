package az.example.eventsapp.mapper;

import az.example.eventsapp.entity.EventEntity;
import az.example.eventsapp.entity.TicketEntity;
import az.example.eventsapp.request.TicketRequest;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {
    List<TicketEntity> toTicketEntities(List<TicketRequest> ticketRequests, @Context EventEntity event);

    @AfterMapping
    default void setEventToTickets(@MappingTarget List<TicketEntity> ticketEntities, @Context EventEntity event) {
        for (TicketEntity ticketEntity : ticketEntities) {
            ticketEntity.setEvent(event);
        }
    }
}
