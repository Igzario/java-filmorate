package ru.yandex.practicum.filmorate.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.message.AppMessageError;
import ru.yandex.practicum.filmorate.message.AppMessageOk;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@RestController
public class FilmController {
    private List<Film> films = new LinkedList<>();
    private int id = 1;

    @GetMapping("/films")
    public List<Film> filmTake() {
        return films;
    }

    @PostMapping("/films")
    public ResponseEntity filmGive(@RequestBody Film film) throws ValidationException {
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
        return new ResponseEntity(new AppMessageOk(201,
                "Добавлен фильм: ", film), HttpStatus.valueOf(201));
    }

    @PutMapping("/films")
    public ResponseEntity filmUpdate(@RequestBody Film film) throws ValidationException {
        try {
            if (!validation(film)) {
                throw new ValidationException();
            }
            boolean isOk = false;
            for (Film film1 : films) {
                if (film.getId() == film1.getId()) {
                    int i = films.indexOf(film1);
                    films.remove(film1);
                    films.add(i, film);
                    isOk = true;
                    log.info("Обновлен фильм: {} ", film);
                    return new ResponseEntity<>(new AppMessageOk(201,
                            "Обновлен фильм:", film), HttpStatus.valueOf(201));
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
        film.setId(id);
        films.add(film);
        id++;
        log.info("Фильм для обновления не найден. Добавлен фильм: {} ", film);
        return new ResponseEntity<>(new AppMessageOk(201,
                "Фильм для обновления не найден. Добавлен фильм: ", film), HttpStatus.valueOf(201));

    }

    private boolean validation(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            return false;
        } else if (film.getDescription().length() > 200) {
            return false;
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            return false;
        } else if (film.getDuration().getSeconds() <= 0) {
            return false;
        }
        return true;
    }
}