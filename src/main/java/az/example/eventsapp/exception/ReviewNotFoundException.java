package az.example.eventsapp.exception;

import org.springframework.http.HttpStatus;

public class ReviewNotFoundException extends GenericException {
    private static final String REVIEW_NOT_FOUND = "Review not found";

    public ReviewNotFoundException() {
        super(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), REVIEW_NOT_FOUND);
    }
}
