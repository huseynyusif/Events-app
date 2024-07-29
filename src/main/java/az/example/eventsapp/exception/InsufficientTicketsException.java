package az.example.eventsapp.exception;

import org.springframework.http.HttpStatus;

public class InsufficientTicketsException extends GenericException {
    private static final String INSUFFICIENT_TICKETS = "Not enough tickets available";

    public InsufficientTicketsException() {
        super(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), INSUFFICIENT_TICKETS);
    }
}
