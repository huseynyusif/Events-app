package az.example.eventsapp.constraint;

import az.example.eventsapp.constraint.validation.UniqueTelephoneNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static az.example.eventsapp.constant.ValidationMessageConstants.TELEPHONE_REGEX;
import static az.example.eventsapp.constant.ValidationMessageConstants.TELEPHONE_REGEX_MESSAGE;

@Pattern(regexp = TELEPHONE_REGEX, message = TELEPHONE_REGEX_MESSAGE)
@Constraint(validatedBy = UniqueTelephoneNumberValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface TelephoneNumber {
    String message() default "Telephone number already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
