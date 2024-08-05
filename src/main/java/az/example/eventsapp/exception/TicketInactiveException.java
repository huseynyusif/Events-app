package az.example.eventsapp.exception;

import org.springframework.http.HttpStatus;

public class TicketInactiveException extends GenericException{
    private static final String INACTIVE_TICKET = "Cannot purchase tickets for an inactive event";

    public TicketInactiveException() {
        super(HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.value(), INACTIVE_TICKET);
    }
}
