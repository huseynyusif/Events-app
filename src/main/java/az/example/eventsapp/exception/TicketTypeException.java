package az.example.eventsapp.exception;

import org.springframework.http.HttpStatus;

public class TicketTypeException extends GenericException{

    private static final String TICKET_TYPE = "Ticket type not found for this event";

    public TicketTypeException() {
        super(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), TICKET_TYPE);
    }
}
