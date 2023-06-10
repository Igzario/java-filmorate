package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Slf4j
@Service
public class GenreService {
    GenreStorage genreStorage;

    @Autowired
    public GenreService(@Qualifier("genreDbStorage") GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public ResponseEntity getGenre(int id) throws EntityNotFoundException {
        Genre genre = genreStorage.getGenre(id);
        log.info("Отправлен жанр: {}", genre);
        return new ResponseEntity<>(genre, HttpStatus.valueOf(200));
    }

    public ResponseEntity getAllGenre() throws EntityNotFoundException {
        List<Genre> genreList = genreStorage.getAllGenre();
        log.info("Отправлен список жанров: {}", genreList);
        return new ResponseEntity<>(genreList, HttpStatus.valueOf(200));
    }
}