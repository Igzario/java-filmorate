package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikeService {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;

    public ResponseEntity likeFilm(@PathVariable int filmId, @PathVariable int userId) throws EntityNotFoundException {
        final User user = userDbStorage.getUser(userId);
        final Film film = filmDbStorage.getFilm(filmId);
        if (user == null) {
            throw new EntityNotFoundException("Фильм");
        } else if (film == null) {
            throw new EntityNotFoundException("Пользователь");
        } else {
            return filmDbStorage.addLike(film, user);
        }
    }

    public ResponseEntity deleteLikeFilm(@PathVariable int id, @PathVariable int userId) throws EntityNotFoundException {
        final User user = userDbStorage.getUser(userId);
        final Film film = filmDbStorage.getFilm(id);
        if (user == null) {
            throw new EntityNotFoundException("Пользователь");
        } else if (film == null) {
            throw new EntityNotFoundException("Фильм");
        } else {
            return filmDbStorage.deleteLike(film, user);
        }
    }
}