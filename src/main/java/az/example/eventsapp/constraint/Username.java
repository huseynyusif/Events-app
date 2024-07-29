package az.example.eventsapp.constraint;

import az.example.eventsapp.constraint.validation.UniqueUsernameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.*;

import static az.example.eventsapp.constant.ValidationMessageConstants.USERNAME_REGEX;
import static az.example.eventsapp.constant.ValidationMessageConstants.USERNAME_REGEX_MESSAGE;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@NotBlank
@NotNull
@Pattern(regexp = USERNAME_REGEX, message = USERNAME_REGEX_MESSAGE)
@Constraint(validatedBy = {UniqueUsernameValidator.class})
public @interface Username {

    String message() default "Username already exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}