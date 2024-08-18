package az.example.eventsapp.exception;

import org.springframework.http.HttpStatus;

public class EmailExistsException extends GenericException{
    private static final String EMAIL_EXISTS = "CustomEmail already exists";

    public EmailExistsException() {
        super(HttpStatus.CONFLICT, HttpStatus.CONFLICT.value(), EMAIL_EXISTS);
    }
}
