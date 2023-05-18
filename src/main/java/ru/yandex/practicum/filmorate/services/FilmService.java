package ru.yandex.practicum.filmorate.services;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.message.AppMessageError;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class FilmService {
    private final List<Film> films = new LinkedList<>();
    private int id = 1;
    public List<Film> getFilms() {
        return films;
    }

    public ResponseEntity filmPost(Film film) {
        try {
            if (!validation(film)) {
                throw new ValidationException();
            }
            film.setId(id);
            films.add(film);
            id++;
            log.info("Добавлен фильм: {} ", film);
        } catch (Exception exception) {
            String error = exception.getMessage();
            if (exception.getClass() == ValidationException.class) {
                log.error(exception.getMessage());
                return new ResponseEntity<>(new AppMessageError(400, error), HttpStatus.BAD_REQUEST);
            } else {
                log.error("Не предвиденная ошибка: " + exception.toString());
                return new ResponseEntity<>(new AppMessageError(500, error), HttpStatus.valueOf(500));
            }
        }
        return new ResponseEntity<>(film, HttpStatus.valueOf(201));
    }

    public ResponseEntity filmPut(Film film) {
        try {
            if (!validation(film)) {
                throw new ValidationException();
            }
            for (Film film1 : films) {
                if (film.getId() == film1.getId()) {
                    int i = films.indexOf(film1);
                    films.remove(film1);
                    films.add(i, film);
                    log.info("Обновлен фильм: {} ", film);
                    return new ResponseEntity<>(film, HttpStatus.valueOf(200));
                }
            }
        } catch (Exception exception) {
            String error = exception.getMessage();
            if (exception.getClass() == ValidationException.class) {
                log.error(exception.getMessage());
                return new ResponseEntity<>(new AppMessageError(400, error), HttpStatus.BAD_REQUEST);
            } else {
                log.error("Не предвиденная ошибка: " + exception.getMessage());
                return new ResponseEntity<>(new AppMessageError(500, error), HttpStatus.valueOf(500));
            }
        }
        log.error("Не найден Film для обновления");
        return new ResponseEntity<>(film, HttpStatus.valueOf(500));
    }

    private boolean validation(Film film) {
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            return false;
        }
        return true;
    }
}
