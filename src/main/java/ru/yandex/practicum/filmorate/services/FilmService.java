package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.SqlException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmService {
    private final FilmDbStorage filmDbStorage;

    public ResponseEntity returnPopularFilms(int count) throws EntityNotFoundException {
        try {
            return filmDbStorage.returnPopularFilms(count);
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity getFilm(long id) throws EntityNotFoundException {
        final Film film = filmDbStorage.getFilm(id);
        if (film != null) {
            return new ResponseEntity<>(film, HttpStatus.valueOf(200));
        }
        throw new EntityNotFoundException("Фильм");
    }

    public ResponseEntity addFilm(@Valid @RequestBody Film film) throws ValidationException, SqlException {
        return filmDbStorage.addFilm(film);
    }

    public ResponseEntity updateFilm(@Valid @RequestBody Film film) throws ValidationException, SqlException, EntityNotFoundException {
        return filmDbStorage.updateFilm(film);
    }

    public List getFilms() throws EntityNotFoundException {
        return filmDbStorage.filmsGet();
    }
}