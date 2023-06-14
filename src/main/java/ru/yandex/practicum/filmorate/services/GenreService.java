package ru.yandex.practicum.filmorate.services;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    public ResponseEntity getGenre(int id) throws EntityNotFoundException {
        Genre genre = genreDbStorage.getGenre(id);
        log.info("Отправлен жанр: {}", genre);
        return new ResponseEntity<>(genre, HttpStatus.valueOf(200));
    }

    public ResponseEntity getAllGenre() throws EntityNotFoundException {
        List<Genre> genreList = genreDbStorage.getAllGenre();
        log.info("Отправлен список жанров: {}", genreList);
        return new ResponseEntity<>(genreList, HttpStatus.valueOf(200));
    }
}