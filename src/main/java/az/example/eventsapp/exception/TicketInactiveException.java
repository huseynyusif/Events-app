package az.example.eventsapp.exception;

import org.springframework.http.HttpStatus;

public class TicketInactiveException extends GenericException{
    private static final String INACTIVE_TICKET = "User is not authorized to perform this action";

    public TicketInactiveException() {
        super(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), INACTIVE_TICKET);
    }
}
