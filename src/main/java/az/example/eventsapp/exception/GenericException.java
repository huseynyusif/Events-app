package az.example.eventsapp.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@Builder
@Getter
public class GenericException extends RuntimeException{
    private final HttpStatus httpStatus;
    private final Integer errorCode;
    private final String errorMessage;
}
