package ru.yandex.practicum.filmorate.exception;

public class UserNotFoundExceptinon extends Exception{
    public UserNotFoundExceptinon() {
        super("Error: Пользователь не найден.");
    }
}
