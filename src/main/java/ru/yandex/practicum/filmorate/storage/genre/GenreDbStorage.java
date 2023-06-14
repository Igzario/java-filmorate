package ru.yandex.practicum.filmorate.storage.genre;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenre(int id) throws EntityNotFoundException {
        Genre genre;
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "select NAME from GENRES WHERE GENRE_ID = ?", id);
        if (userRows.next()) {
            String nameGenre = userRows.getString("NAME");
            genre = new Genre(id, nameGenre);
        } else {
            throw new EntityNotFoundException("Genre");
        }
        log.info("Отправлен жанр: {} ", genre);
        return genre;
    }

    @Override
    public List<Genre> getAllGenre() throws EntityNotFoundException {
        List<Genre> genreList = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "select GENRE_ID from GENRES GROUP BY GENRE_ID");
        if (userRows.next()) {
            userRows.previous();
            while (userRows.next()) {
                int genreId = Integer.parseInt(Objects.requireNonNull(userRows.getString("GENRE_ID")));
                genreList.add(getGenre(genreId));
            }
        } else {
            throw new EntityNotFoundException("Genre");
        }
        log.info("Отправлен список жанров");
        return genreList;
    }
}