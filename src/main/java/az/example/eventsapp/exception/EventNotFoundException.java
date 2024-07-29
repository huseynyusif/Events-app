package az.example.eventsapp.exception;

import org.springframework.http.HttpStatus;

public class EventNotFoundException extends GenericException{
    private static final String EVENT_NOT_FOUND = "Event not found";

    public EventNotFoundException() {
        super(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), EVENT_NOT_FOUND);
    }
}
