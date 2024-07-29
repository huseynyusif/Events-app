package az.example.eventsapp.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDeleteRequest {
    @NotBlank(message = "Password is required")
    private String password;
}
