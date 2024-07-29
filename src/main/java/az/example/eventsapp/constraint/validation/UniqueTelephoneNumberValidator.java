package az.example.eventsapp.constraint.validation;

import az.example.eventsapp.constraint.TelephoneNumber;
import az.example.eventsapp.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class UniqueTelephoneNumberValidator implements ConstraintValidator<TelephoneNumber, String> {

    private final UserRepository userRepository;

    @Override
    public void initialize(TelephoneNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        return !userRepository.existsByTelephoneNumber(phoneNumber);
    }
}
