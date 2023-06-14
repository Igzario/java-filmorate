package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.services.GenreService;

@RequiredArgsConstructor
@Slf4j
@RestController
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/genres/{id}")
    public ResponseEntity getGenre(@PathVariable int id) throws EntityNotFoundException {
        return genreService.getGenre(id);
    }

    @GetMapping("/genres")
    public ResponseEntity getAllGenre() throws EntityNotFoundException {
        return genreService.getAllGenre();
    }
}