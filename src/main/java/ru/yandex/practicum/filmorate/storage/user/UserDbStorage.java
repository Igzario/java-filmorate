package ru.yandex.practicum.filmorate.storage.user;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
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
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public ResponseEntity userAdd(User user) {
        validName(user);
        Date date = Date.valueOf(user.getBirthday());
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) values(?,?,?,?)";
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setDate(4, date);
            return preparedStatement;
        }, generatedKeyHolder);
        int id = generatedKeyHolder.getKey().intValue();

//        int id = 0;
//        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
//                "select USER_ID as id from USERS WHERE EMAIL = ? AND LOGIN = ? AND NAME = ? AND BIRTHDAY = ? GROUP  BY id", user.getEmail(), user.getLogin(), user.getName(), date);
//        if (userRows.next()) {
//            id = Integer.parseInt(userRows.getString("USER_ID"));
//        }

        user.setId(id);
        log.info("Добавлен пользователь: {} ", user);
        return new ResponseEntity<>(user, HttpStatus.valueOf(201));
    }

    @Override
    public List<User> usersGet() throws EntityNotFoundException {
        List<User> users = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "select USER_ID AS ID from USERS GROUP BY ID");
        if (userRows.next()) {
            int userId = Integer.parseInt(userRows.getString("USER_ID"));
            User user = getUser(userId);
            users.add(user);
        }
        return users;
    }

    @Override
    public ResponseEntity userUpdate(User user) {
        validName(user);
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "select USER_ID AS ID from USERS WHERE USER_ID = ? GROUP BY ID", user.getId());
        if (userRows.next()) {
            Date date = Date.valueOf(user.getBirthday());
            jdbcTemplate.update("UPDATE USERS SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE USER_ID = ?", user.getEmail(), user.getLogin(), user.getName(), date, user.getId());

            log.info("Обновлен пользователь: {} ", user);
            return new ResponseEntity<>(user, HttpStatus.valueOf(200));
        }
        log.error("Не найден пользователь для обновления");
        return new ResponseEntity<>(user, HttpStatus.valueOf(404));
    }

    @Override
    public User getUser(long id) throws EntityNotFoundException {
        String userId = null;
        String name = null;
        String email = null;
        String login = null;
        String birthday = null;

        SqlRowSet userRows =
                jdbcTemplate.queryForRowSet(
                        "select * from USERS WHERE USER_ID = ?", id);

        if (userRows.next()) {
            userId = userRows.getString("USER_ID");
            name = userRows.getString("NAME");
            email = userRows.getString("EMAIL");
            login = userRows.getString("LOGIN");
            birthday = userRows.getString("BIRTHDAY");
        } else {
            throw new EntityNotFoundException("Пользователь");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        User user = new User(email, login);
        user.setName(name);
        if (birthday != null) {
            user.setBirthday(LocalDate.parse(birthday, formatter));
        }
        user.setId(Integer.parseInt(userId));
        return user;
    }

    private void validName(User user) {
        try {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
        } catch (NullPointerException e) {
            user.setName(user.getLogin());
        }
    }
}