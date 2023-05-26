package ru.yandex.practicum.filmorate.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundExceptinon;
import ru.yandex.practicum.filmorate.exception.UserNotFoundExceptinon;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PutMapping("/films/{id}/like/{userId}")
    public ResponseEntity likeFilm(@PathVariable int id, @PathVariable int userId) throws UserNotFoundExceptinon, FilmNotFoundExceptinon {
        return filmService.likeFilm(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public ResponseEntity deleteLikeFilm(@PathVariable int id, @PathVariable int userId) throws UserNotFoundExceptinon, FilmNotFoundExceptinon {
        return filmService.deleteLikeFilm(id, userId);
    }

    @GetMapping("/films/popular")
    public ResponseEntity returnPopularFilms(@RequestParam(defaultValue = "0") Integer count) {
        return filmService.returnPopularFilms(count);
    }

    @GetMapping("/films/{id}")
    public ResponseEntity filmGet(@PathVariable long id) throws FilmNotFoundExceptinon {
        return filmService.filmGet(id);
    }

    @GetMapping("/films")
    public List getFilms() {
        return filmService.getFilms();
    }

    @PostMapping("/films")
    public ResponseEntity filmAdd(@Valid @RequestBody Film film) throws ValidationException {
        return filmService.filmAdd(film);
    }

    @PutMapping("/films")
    public ResponseEntity filmRefresh(@Valid @RequestBody Film film) throws ValidationException {
        return filmService.filmRefresh(film);
    }
}