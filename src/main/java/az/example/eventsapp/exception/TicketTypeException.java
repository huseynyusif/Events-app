package az.example.eventsapp.exception;

import org.springframework.http.HttpStatus;

public class TicketTypeException extends GenericException{

    private static final String TICKET_TYPE = "Ticket type not found for this event";

    public TicketTypeException() {
        super(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), TICKET_TYPE);
    }
}
