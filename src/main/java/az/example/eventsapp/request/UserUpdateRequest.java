package az.example.eventsapp.request;

import az.example.eventsapp.constant.ValidationMessageConstants;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static az.example.eventsapp.constant.ValidationMessageConstants.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    private String name;
    private String surname;

    @Pattern(regexp = TELEPHONE_REGEX, message = TELEPHONE_REGEX_MESSAGE)
    private String telephoneNumber;

    @Pattern(regexp = EMAIL_REGEX, message = EMAIL_REGEX_MESSAGE)
    private String email;

    @Pattern(regexp = PASSWORD_REGEX, message = PASSWORD_REGEX_MESSAGE)
    private String password;
}
