package az.example.eventsapp.exception;

import org.springframework.http.HttpStatus;

public class IncorrectPasswordException extends GenericException{
    private static final String INCORRECT_PASSWORD = "Incorrect password";

    public IncorrectPasswordException() {
        super(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), INCORRECT_PASSWORD);
    }
}
