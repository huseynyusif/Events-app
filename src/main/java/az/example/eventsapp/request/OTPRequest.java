package az.example.eventsapp.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OTPRequest {
    @NotBlank(message = "Username is required")
    private String username;
}
