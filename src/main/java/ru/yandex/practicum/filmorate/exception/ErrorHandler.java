package ru.yandex.practicum.filmorate.exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.Map;

@Slf4j
@ControllerAdvice(("ru.yandex.practicum.filmorate"))
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity userNotFound(final UserNotFoundExceptinon exception){
        log.error(exception.getMessage());
        return new ResponseEntity<>(Map.of("Error: ", exception.getMessage()), HttpStatus.valueOf(404));
    }

    @ExceptionHandler
    public ResponseEntity filmNotFound(final FilmNotFoundExceptinon exception){
        log.error(exception.getMessage());
        return new ResponseEntity<>(Map.of("Error: ", exception.getMessage()), HttpStatus.valueOf(404));
    }

    @ExceptionHandler
    public ResponseEntity validationException(final ValidationException exception){
        log.error(exception.getMessage());
        return new ResponseEntity<>(Map.of("Error: ", exception.getMessage()), HttpStatus.valueOf(400));
    }

    @ExceptionHandler
    public ResponseEntity runtimeException(final RuntimeException exception){
        log.error("Не предвиденная ошибка: " + exception.getMessage());
        return new ResponseEntity<>(Map.of("Не предвиденная ошибка: ", exception.getMessage()), HttpStatus.valueOf(500));
    }
}