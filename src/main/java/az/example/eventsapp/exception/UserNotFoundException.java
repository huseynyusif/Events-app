package az.example.eventsapp.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends GenericException{

    private static final String USER_NOT_FOUND = "User not found";

    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), USER_NOT_FOUND);
    }
}
