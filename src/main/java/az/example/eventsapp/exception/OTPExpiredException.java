package az.example.eventsapp.exception;

import org.springframework.http.HttpStatus;

public class OTPExpiredException extends GenericException{
    private static final String OTP_EXPIRED = "OTP has expired";

    public OTPExpiredException() {
        super(HttpStatus.GONE, HttpStatus.GONE.value(), OTP_EXPIRED);
    }
}
