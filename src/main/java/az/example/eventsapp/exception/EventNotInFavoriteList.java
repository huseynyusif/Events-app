package az.example.eventsapp.exception;

import org.springframework.http.HttpStatus;

public class EventNotInFavoriteList extends GenericException{
    private static final String EVENT_NOT_FOUND = "Event is not in the favorite list";

    public EventNotInFavoriteList() {
        super(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), EVENT_NOT_FOUND);
    }
}
