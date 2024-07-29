package az.example.eventsapp.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedActionException extends GenericException{
    private static final String UNAUTHORIZED_ACTION = "User is not authorized to perform this action";

    public UnauthorizedActionException() {
        super(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), UNAUTHORIZED_ACTION);
    }
}
