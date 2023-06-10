package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.SqlException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@Component
public class FilmController {
    private final FilmService filmService;

    @PutMapping("/films/{id}/like/{userId}")
    public ResponseEntity likeFilm(@PathVariable int id, @PathVariable int userId) throws EntityNotFoundException, SqlException {
        return filmService.likeFilm(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public ResponseEntity deleteLikeFilm(@PathVariable int id, @PathVariable int userId) throws EntityNotFoundException, SqlException {
        return filmService.deleteLikeFilm(id, userId);
    }

    @GetMapping("/films/popular")
    public ResponseEntity returnPopularFilms(@RequestParam(defaultValue = "100") Integer count) throws SqlException {
        try {
            return filmService.returnPopularFilms(count);
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/films/{id}")
    public ResponseEntity getFilm(@PathVariable long id) throws EntityNotFoundException, SqlException {
        return filmService.filmGet(id);
    }

    @GetMapping("/films")
    public List getFilms() throws EntityNotFoundException {
        return filmService.getFilms();
    }

    @PostMapping("/films")
    public ResponseEntity addFilm(@Valid @RequestBody Film film) throws ValidationException, SqlException {
        return filmService.filmAdd(film);
    }

    @PutMapping("/films")
    public ResponseEntity updateFilm(@Valid @RequestBody Film film) throws ValidationException, SqlException, EntityNotFoundException {
        return filmService.filmRefresh(film);
    }
}