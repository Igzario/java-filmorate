package ru.yandex.practicum.filmorate.storage.film;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Data
@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public ResponseEntity filmAdd(Film film) throws ValidationException {
        validation(film);
        Date date = Date.valueOf(film.getReleaseDate());
        Integer duration = Integer.parseInt(String.valueOf(film.getDuration()));
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO FILMS (MPA_ID, NAME, DESCRIPTION, RELIASEDATE, DURATION) values(?,?,?,?,?)";
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, film.getMpa().getId());
            preparedStatement.setString(2, film.getName());
            preparedStatement.setString(3, film.getDescription());
            preparedStatement.setDate(4, date);
            preparedStatement.setInt(5, duration);
            return preparedStatement;
        }, generatedKeyHolder);
        int id = Objects.requireNonNull(generatedKeyHolder.getKey()).intValue();
        film.setId(id);
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update("INSERT INTO FILMSGENRE (FILM_ID, GENRE_ID) values(?,?)", film.getId(), genre.getId());
        }

        log.info("Добавлен фильм: {} ", film);
        return new ResponseEntity<>(film, HttpStatus.valueOf(201));
    }

    @Override
    public List<Film> filmsGet() throws EntityNotFoundException {
        List<Film> films = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "select FILM_ID AS ID from FILMS GROUP BY ID");
        while (userRows.next()) {
            int filmId = Integer.parseInt(userRows.getString("FILM_ID"));
            Film film = getFilm(filmId);
            films.add(film);
        }
        return films;
    }

    @Override
    public ResponseEntity filmUpdate(Film film) throws ValidationException, EntityNotFoundException {
        validation(film);
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "select FILM_ID AS ID from FILMS WHERE FILM_ID = ? GROUP BY ID", film.getId());
        if (userRows.next()) {
            Date date = Date.valueOf(film.getReleaseDate());
            Integer duration = Integer.parseInt(String.valueOf(film.getDuration()));
            jdbcTemplate.update("UPDATE FILMS SET MPA_ID = ?, NAME = ?, DESCRIPTION = ?, RELIASEDATE = ?, DURATION = ?  WHERE FILM_ID = ?", film.getMpa().getId(), film.getName(), film.getDescription(), date, duration, film.getId());

            userRows = jdbcTemplate.queryForRowSet(
                    "select * from FILMSGENRE WHERE FILM_ID = ?", film.getId());
            while (userRows.next()) {
                int genreId = Integer.parseInt(Objects.requireNonNull(userRows.getString("GENRE_ID")));
                jdbcTemplate.update("DELETE FROM FILMSGENRE WHERE FILM_ID = ? AND GENRE_ID = ?", film.getId(), genreId);
            }

            List<Integer> testList = new ArrayList<>();
            for (Genre genre : film.getGenres()) {
                if (!testList.contains(genre.getId())) {
                    testList.add(genre.getId());
                    jdbcTemplate.update("INSERT INTO FILMSGENRE (FILM_ID, GENRE_ID) values(?,?)", film.getId(), genre.getId());
                }
            }
            Film newFilm = getFilm(film.getId());
            log.info("Обновлен фильм: {} ", newFilm);
            return new ResponseEntity<>(newFilm, HttpStatus.valueOf(200));
        }
        log.error("Не найден Film для обновления");
        return new ResponseEntity<>(film, HttpStatus.valueOf(404));
    }

    @Override
    public Film getFilm(long id) throws EntityNotFoundException {
        String film_id = null;
        String name = null;
        String description = null;
        String releaseDate = null;
        String duration = null;

        int mpaId;
        String mpaName;
        Mpa mpa;
        Genre genre;
        String genreName;
        int genreId;
        List<Genre> genres = new ArrayList();

        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "select F.FILM_ID,M.MPA_ID,F.NAME as FILM_NAME,DESCRIPTION,RELIASEDATE,DURATION, M.NAME as MPA_NAME from FILMS as f left join  MPA M on M.MPA_ID = f.MPA_ID WHERE F.FILM_ID = ? GROUP BY f.FILM_ID", id);

        if (userRows.next()) {
            film_id = userRows.getString("FILM_ID");
            name = userRows.getString("FILM_NAME");
            description = userRows.getString("DESCRIPTION");
            releaseDate = userRows.getString("RELIASEDATE");
            duration = userRows.getString("DURATION");


            mpaId = Integer.parseInt(userRows.getString("MPA_ID"));
            mpaName = userRows.getString("MPA_NAME");
            mpa = new Mpa(mpaId, mpaName);

            SqlRowSet userRows2 = jdbcTemplate.queryForRowSet("SELECT FG.FILM_ID, FG.GENRE_ID, G.NAME as GENRE_NAME FROM FILMSGENRE as FG  LEFT JOIN GENRES AS G ON FG.GENRE_ID = G.GENRE_ID WHERE FILM_ID = ? GROUP BY FG.FILM_ID, FG.GENRE_ID", film_id);
            while (userRows2.next()) {
                genreId = Integer.parseInt(Objects.requireNonNull(userRows2.getString("GENRE_ID")));
                genreName = userRows2.getString("GENRE_NAME");
                genre = new Genre(genreId, genreName);
                genres.add(genre);
            }

        } else {
            throw new EntityNotFoundException("Фильм");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Film film = new Film(name, description, LocalDate.parse(releaseDate, formatter), Integer.parseInt(duration), mpa);
        film.setId(Integer.parseInt(film_id));

        film.setGenres(genres);
        log.info("Отправлен фильм: {} ", film);
        return film;
    }

    @Override
    public ResponseEntity returnPopularFilms(int count) throws EntityNotFoundException {
        final List<Film> popularFilms = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "select F.FILM_ID, COUNT(LIKE_ID) AS many from  FILMS F LEFT JOIN LIKES L on F.FILM_ID = L.FILM_ID GROUP BY F.FILM_ID ORDER BY many DESC LIMIT ?", count);

        while (userRows.next()) {
            int film_id = Integer.parseInt(userRows.getString("FILM_ID"));
            Film film = getFilm(film_id);
            popularFilms.add(film);
        }

        log.info("Отправлен список популярных фильмов размеров: {} ", count);
        return new ResponseEntity<>(popularFilms, HttpStatus.valueOf(200));
    }

    @Override
    public ResponseEntity addLike(Film film, User user) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "select * from LIKES WHERE FILM_ID = ? AND USER_ID = ?", film.getId(), user.getId());

        if (userRows.next()) {
            log.error("Этот пользователь уже поставил лайк этому фильму");
            return new ResponseEntity<>(film, HttpStatus.valueOf(404));
        }
        jdbcTemplate.update("INSERT INTO LIKES (FILM_ID, USER_ID) values(?,?)", film.getId(), user.getId());
        return new ResponseEntity<>(film, HttpStatus.valueOf(200));
    }

    private void validation(Film film) throws ValidationException {
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException();
        }
    }
}