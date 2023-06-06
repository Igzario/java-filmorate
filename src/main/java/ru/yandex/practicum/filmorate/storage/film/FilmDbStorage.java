package ru.yandex.practicum.filmorate.storage.film;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
@Data
public class FilmDbStorage implements FilmStorage{
    private final JdbcTemplate jdbcTemplate;

    @Override
    public ResponseEntity filmAdd(Film film) throws ValidationException {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("insert INSERT INTO Films (genre_id, rating_id, name, description, releaseDate, duration) " +
                "VALUES (select genre_id from Genre where name=?))" , film.get;

jdbcTemplate.query();


    }

    @Override
    public List<Film> filmsGet() {
        return null;
    }

    @Override
    public ResponseEntity filmRefresh(Film film) throws ValidationException {
        return null;
    }

    @Override
    public Film getFilm(long id) {
        return null;
    }
}
