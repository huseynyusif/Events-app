package az.example.eventsapp.exception;

import org.springframework.http.HttpStatus;

public class PaymentFailedException extends GenericException {
    private static final String PAYMENT_FAILED = "Payment processing failed";

    public PaymentFailedException() {
        super(HttpStatus.PAYMENT_REQUIRED, HttpStatus.PAYMENT_REQUIRED.value(), PAYMENT_FAILED);
    }
}
