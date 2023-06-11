package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.SqlException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmStorage {

    ResponseEntity addFilm(Film film) throws ValidationException, SqlException;

    List<Film> filmsGet() throws EntityNotFoundException;

    ResponseEntity updateFilm(Film film) throws ValidationException, SqlException, EntityNotFoundException;

    Film getFilm(long id) throws EntityNotFoundException;

    ResponseEntity returnPopularFilms(int count) throws EntityNotFoundException;

    ResponseEntity addLike(Film film, User user);

    ResponseEntity deleteLike(Film film, User user);
}