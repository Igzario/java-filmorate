package ru.yandex.practicum.filmorate.services;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundExceptinon;
import ru.yandex.practicum.filmorate.exception.UserNotFoundExceptinon;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public ResponseEntity likeFilm(@PathVariable int id, @PathVariable int userId) throws UserNotFoundExceptinon, FilmNotFoundExceptinon {
        final User user = userStorage.getUser(userId);
        final Film film = filmStorage.getFilm(id);
        if (user == null) {
            throw new UserNotFoundExceptinon();
        } else if (film == null) {
            throw new FilmNotFoundExceptinon();
        } else {
            log.info("Пользователь {} поставил лайк фильму: {} ", user, film);
            film.getLikes().put(userId, user.getName());
            return new ResponseEntity<>(film, HttpStatus.valueOf(200));
        }
    }

    public ResponseEntity deleteLikeFilm(@PathVariable int id, @PathVariable int userId) throws UserNotFoundExceptinon, FilmNotFoundExceptinon {
        final User user = userStorage.getUser(userId);
        final Film film = filmStorage.getFilm(id);
        if (user == null) {
            throw new UserNotFoundExceptinon();
        } else if (film == null) {
            throw new FilmNotFoundExceptinon();
        } else {
            log.info("Пользователь {} удалил лайк фильму: {} ", user, film);
            film.getLikes().remove(userId);
            return new ResponseEntity<>(film, HttpStatus.valueOf(204));
        }
    }

    public ResponseEntity returnPopularFilms(int count) {
        final List<Film> popularFilms;
        if (count == 0) {
            count = 10;
        }
        if (filmStorage.filmsGet().size() < count) {
            count = filmStorage.filmsGet().size();
        }
        popularFilms = filmStorage.filmsGet().subList(0, count);
        log.info("Отправлен список популярных фильмов размеров: {} ", count);
        return new ResponseEntity<>(popularFilms, HttpStatus.valueOf(200));
    }

    public ResponseEntity filmGet(long id) throws FilmNotFoundExceptinon {
        final Film film = filmStorage.getFilm(id);
        if (film != null) {
            return new ResponseEntity<>(film, HttpStatus.valueOf(200));
        }
        throw new FilmNotFoundExceptinon();
    }

    public ResponseEntity filmAdd(@Valid @RequestBody Film film) throws ValidationException {
        return filmStorage.filmAdd(film);
    }

    public ResponseEntity filmRefresh(@Valid @RequestBody Film film) throws ValidationException {
        return filmStorage.filmRefresh(film);
    }

    public List getFilms() {
        return filmStorage.filmsGet();
    }
}
