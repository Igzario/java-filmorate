package ru.yandex.practicum.filmorate.exception;

public class ValidationException extends Exception {
    public ValidationException() {
        super("Ошибка ввода, попробуйте еще раз");
    }
}
