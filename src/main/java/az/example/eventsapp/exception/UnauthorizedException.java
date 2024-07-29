package az.example.eventsapp.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends GenericException {

    private static final String UNAUTHORIZED_ACTION = "You are not authorized to perform this action";

    public UnauthorizedException() {
        super(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), UNAUTHORIZED_ACTION);
    }
}
