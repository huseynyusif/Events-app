package az.example.eventsapp.request;

import az.example.eventsapp.constraint.Email;
import az.example.eventsapp.constraint.Password;
import az.example.eventsapp.constraint.TelephoneNumber;
import az.example.eventsapp.constraint.Username;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @Email
    private String email;

    @Username
    private String username;

    @Password
    private String password;

}
