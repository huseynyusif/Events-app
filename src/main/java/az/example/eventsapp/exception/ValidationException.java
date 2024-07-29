package az.example.eventsapp.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@AllArgsConstructor
@Getter
public class ValidationException extends RuntimeException{
    private final HttpStatus httpStatus;
    private final Integer errorCode;
    private final String errorMessage;
    private final LocalDateTime timestamp;
    private final Map<String, String> errors;
}

