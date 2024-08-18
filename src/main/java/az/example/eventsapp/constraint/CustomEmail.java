package az.example.eventsapp.constraint;

import az.example.eventsapp.constraint.validation.UniqueEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.*;

import static az.example.eventsapp.constant.ValidationMessageConstants.EMAIL_REGEX;
import static az.example.eventsapp.constant.ValidationMessageConstants.EMAIL_REGEX_MESSAGE;

@Pattern(regexp = EMAIL_REGEX, message = EMAIL_REGEX_MESSAGE)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@NotBlank
@Constraint(validatedBy = {UniqueEmailValidator.class})
public @interface CustomEmail {
    String message() default "CustomEmail must be unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
