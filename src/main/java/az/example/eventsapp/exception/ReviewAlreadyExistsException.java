package az.example.eventsapp.exception;

import org.springframework.http.HttpStatus;

public class ReviewAlreadyExistsException extends GenericException {
    private static final String REVIEW_ALREADY_EXISTS = "User has already left a comment for this event";

    public ReviewAlreadyExistsException() {
        super(HttpStatus.CONFLICT, HttpStatus.CONFLICT.value(), REVIEW_ALREADY_EXISTS);
    }
}
