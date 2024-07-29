package az.example.eventsapp.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OTPVerificationRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "OTP is required")
    private String otp;
}
