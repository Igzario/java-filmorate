package ru.yandex.practicum.filmorate.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class FilmController {

    FilmService filmService = new FilmService();

    @GetMapping("/films")
    public List filmTake() {
        return filmService.getFilms();
    }

    @PostMapping("/films")
    public ResponseEntity ilmGive(@Valid @RequestBody Film film) throws ValidationException {
        return filmService.filmPost(film);
    }

    @PutMapping("/films")
    public ResponseEntity filmUpdate(@RequestBody Film film) throws ValidationException {
        return filmService.filmPut(film);
    }
}