package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
class FilmorateApplicationTests {
    FilmDbStorage filmStorage;
    UserDbStorage userStorage;

    @Autowired
    public FilmorateApplicationTests(FilmDbStorage filmStorage, UserDbStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Test
    public void testFilm() throws EntityNotFoundException {
        Date date = Date.valueOf(LocalDate.of(2020, 5, 6));
        Mpa mpa = new Mpa(2, "Ужас");
        int duration = 100;
        Film film = new Film();
        film.setName("film");
        film.setDescription("descriptionFilm");
        film.setReleaseDate(LocalDate.of(2020, 5, 6));
        film.setMpa(mpa);
        film.setDuration(100);
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO FILMS (MPA_ID, NAME, DESCRIPTION, RELIASEDATE, DURATION) values(?,?,?,?,?)";
        int rowsAffected = filmStorage.getJdbcTemplate().update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, film.getMpa().getId());
            preparedStatement.setString(2, film.getName());
            preparedStatement.setString(3, film.getDescription());
            preparedStatement.setDate(4, date);
            preparedStatement.setInt(5, duration);
            return preparedStatement;
        }, generatedKeyHolder);
        int id = Objects.requireNonNull(generatedKeyHolder.getKey()).intValue();
        Film filmOptional = filmStorage.getFilm(1);
        System.out.println(" ");
        assertEquals(film, filmOptional);
    }

    @Test
    public void testUser() throws EntityNotFoundException {
        User user = new User();
        user.setEmail("ya1@Ya.ru");
        user.setLogin("login1");
        user.setBirthday(LocalDate.of(1990, 5, 6));
        Date date = Date.valueOf(user.getBirthday());
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) values(?,?,?,?)";
        int rowsAffected = userStorage.getJdbcTemplate().update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setDate(4, date);
            return preparedStatement;
        }, generatedKeyHolder);
        int id = generatedKeyHolder.getKey().intValue();

        user.setId(id);
        User userOptional = userStorage.getUser(1);
        assertEquals(user, userOptional);
    }
}