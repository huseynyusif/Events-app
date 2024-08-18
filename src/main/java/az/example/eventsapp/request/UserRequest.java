package az.example.eventsapp.request;

import az.example.eventsapp.constraint.CustomEmail;
import az.example.eventsapp.constraint.Password;
import az.example.eventsapp.constraint.TelephoneNumber;
import az.example.eventsapp.constraint.Username;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Surname is required")
    private String surname;

    @TelephoneNumber
    private String telephoneNumber;

    @CustomEmail
    private String email;

    @Username
    private String username;

    @Password
    private String password;

}
