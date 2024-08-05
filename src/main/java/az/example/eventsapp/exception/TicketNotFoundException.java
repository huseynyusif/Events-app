package az.example.eventsapp.exception;

import org.springframework.http.HttpStatus;

public class TicketNotFoundException extends GenericException{
    private static final String TICKET_NOT_FOUND = "Payment processing failed";

    public TicketNotFoundException() {
        super(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), TICKET_NOT_FOUND);
    }
}
