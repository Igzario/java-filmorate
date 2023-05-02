package ru.yandex.practicum.filmorate.exception;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationException extends Exception {
    public ValidationException() {
        super("Ошибка ввода, попробуйте еще раз");
    }
}
