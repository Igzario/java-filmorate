package ru.yandex.practicum.filmorate.exception;

public class EntityNotFoundException extends Exception {
    public EntityNotFoundException(String obj) {
        super("Error: " + obj + " не найден");
    }

}
