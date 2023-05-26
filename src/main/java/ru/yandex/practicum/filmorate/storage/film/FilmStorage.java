package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    ResponseEntity filmAdd(Film film) throws ValidationException;

    List<Film> filmsGet();

    ResponseEntity filmRefresh(Film film) throws ValidationException;

    Film getFilm(long id);
}
