package az.example.eventsapp.exception;

import org.springframework.http.HttpStatus;

public class InvalidOTPException extends GenericException{
    private static final String INVALID_OTP = "Invalid OTP or username";

    public InvalidOTPException() {
        super(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), INVALID_OTP);
    }
}
