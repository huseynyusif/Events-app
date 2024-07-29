package az.example.eventsapp.exception;

import org.springframework.http.HttpStatus;

public class OTPNotVerifiedException extends GenericException{
    private static final String OTP_NOT_VERIFIED = "OTP not verified";

    public OTPNotVerifiedException() {
        super(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), OTP_NOT_VERIFIED);
    }
}
