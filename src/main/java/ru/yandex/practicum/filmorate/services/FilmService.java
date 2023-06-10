package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.SqlException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public ResponseEntity likeFilm(@PathVariable int filmId, @PathVariable int userId) throws EntityNotFoundException {
        final User user = userStorage.getUser(userId);
        final Film film = filmStorage.getFilm(filmId);
        if (user == null) {
            throw new EntityNotFoundException("Фильм");
        } else if (film == null) {
            throw new EntityNotFoundException("Пользователь");
        } else {
            log.info("Пользователь {} поставил лайк фильму: {} ", user, film);
            return filmStorage.addLike(film, user);
        }
    }

    public ResponseEntity deleteLikeFilm(@PathVariable int id, @PathVariable int userId) throws EntityNotFoundException {
        final User user = userStorage.getUser(userId);
        final Film film = filmStorage.getFilm(id);
        if (user == null) {
            throw new EntityNotFoundException("Пользователь");
        } else if (film == null) {
            throw new EntityNotFoundException("Фильм");
        } else {
            log.info("Пользователь {} удалил лайк фильму: {} ", user, film);
            film.getLikes().remove(userId);
            return new ResponseEntity<>(film, HttpStatus.valueOf(200));
        }
    }

    public ResponseEntity returnPopularFilms(int count) throws EntityNotFoundException {
        return filmStorage.returnPopularFilms(count);
    }

    public ResponseEntity filmGet(long id) throws EntityNotFoundException {
        final Film film = filmStorage.getFilm(id);
        if (film != null) {
            return new ResponseEntity<>(film, HttpStatus.valueOf(200));
        }
        throw new EntityNotFoundException("Фильм");
    }

    public ResponseEntity filmAdd(@Valid @RequestBody Film film) throws ValidationException, SqlException {
        return filmStorage.filmAdd(film);
    }

    public ResponseEntity filmRefresh(@Valid @RequestBody Film film) throws ValidationException, SqlException, EntityNotFoundException {
        return filmStorage.filmUpdate(film);
    }

    public List getFilms() throws EntityNotFoundException {
        return filmStorage.filmsGet();
    }
}