package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final List<Film> films = new LinkedList<>();
    private int id = 1;

    @Override
    public ResponseEntity filmAdd(Film film) throws ValidationException {
        validation(film);
        film.setId(id);
        films.add(film);
        id++;
        log.info("Добавлен фильм: {} ", film);
        return new ResponseEntity<>(film, HttpStatus.valueOf(201));
    }

    @Override
    public List<Film> filmsGet() {
        Comparator<Film> comparator = Comparator.comparing(obj -> obj.getLikes().size());
        films.sort(Collections.reverseOrder(comparator));
        return films;
    }

    @Override
    public ResponseEntity filmRefresh(Film film) throws ValidationException {
        validation(film);
        for (Film film1 : films) {
            if (film.getId() == film1.getId()) {
                int i = films.indexOf(film1);
                films.remove(film1);
                films.add(i, film);
                log.info("Обновлен фильм: {} ", film);
                return new ResponseEntity<>(film, HttpStatus.valueOf(200));
            }
        }
        log.error("Не найден Film для обновления");
        return new ResponseEntity<>(film, HttpStatus.valueOf(500));
    }

    @Override
    public Film getFilm(long id) {
        Film film = null;
        for (Film findFilm : films) {
            if (findFilm.getId() == id) {
                film = findFilm;
                break;
            }
        }
        return film;
    }

    private void validation(Film film) throws ValidationException {
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException();
        }
    }
}