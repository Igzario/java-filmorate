package ru.yandex.practicum.filmorate.exception;

public class FilmNotFoundExceptinon extends Exception {
    public FilmNotFoundExceptinon() {
        super("Error: Фильм не найден.");
    }
}
