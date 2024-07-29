package az.example.eventsapp.exception;

import org.springframework.http.HttpStatus;

public class TelephoneNumberExistsException extends GenericException{
    private static final String TELEPHONE_NUMBER_EXISTS = "Telephone number already exists";

    public TelephoneNumberExistsException() {
        super(HttpStatus.CONFLICT, HttpStatus.CONFLICT.value(), TELEPHONE_NUMBER_EXISTS);
    }
}
