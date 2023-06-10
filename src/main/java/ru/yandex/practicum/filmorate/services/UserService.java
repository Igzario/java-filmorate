package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
        if (userStorage.getClass().equals(UserDbStorage.class)) {
            this.jdbcTemplate = ((UserDbStorage) userStorage).getJdbcTemplate();
        } else jdbcTemplate = null;
    }

    public ResponseEntity addFriend(long userId, long friendId) throws EntityNotFoundException {
        final User user = userStorage.getUser(userId);
        final User userFriend = userStorage.getUser(friendId);
        String status;
        SqlRowSet userRows =
                jdbcTemplate.queryForRowSet(
                        "select * from FRIENDS WHERE USER_1 = ? AND USER_2 = ?", userId, friendId);
        if (userRows.next()) {
            log.info("Пользователь {} уже в списке друзей {} ", user, userFriend);
            return new ResponseEntity<>(user, HttpStatus.valueOf(200));
        } else {
            userRows =
                    jdbcTemplate.queryForRowSet(
                            "select * from FRIENDS WHERE USER_1 = ? AND USER_2 = ?", friendId, userId);
        }
        if (userRows.next()) {
            status = userRows.getString("STATUS");
            if (status.equals("TRUE")) {
                log.info("Пользователь {} уже в списке друзей {}, дружба подтверждена ", user, userFriend);
                return new ResponseEntity<>(user, HttpStatus.valueOf(200));
            } else {
                log.info("Пользователь {} подтвердил дружбу с {}", user, userFriend);
                jdbcTemplate.update("UPDATE FRIENDS SET STATUS = true WHERE USER_1 = ? AND USER_2 = ?", friendId, userId);
                return new ResponseEntity<>(user, HttpStatus.valueOf(200));
            }
        }
        jdbcTemplate.update("INSERT INTO FRIENDS (USER_1, USER_2, STATUS) values (?,?,?)", userId, friendId, false);
        log.info("Пользователю: {}, добавлен друг {} ", user, userFriend);
        return new ResponseEntity<>(user, HttpStatus.valueOf(200));
    }

    public ResponseEntity getFriendsForId(long id) throws EntityNotFoundException {
        final User user = userStorage.getUser(id);
        int idFriend;
        SqlRowSet userRows =
                jdbcTemplate.queryForRowSet(
                        "select * from FRIENDS WHERE USER_1 = ?", id);
        while (userRows.next()) {
            idFriend = Integer.parseInt(userRows.getString("USER_2"));
            User userFriend = userStorage.getUser(idFriend);
            user.getFriends().add(userFriend);
        }
        userRows =
                jdbcTemplate.queryForRowSet(
                        "select * from FRIENDS WHERE USER_2 = ? AND STATUS = true", id);
        while (userRows.next()) {
            idFriend = Integer.parseInt(userRows.getString("USER_1"));
            User userFriend = userStorage.getUser(idFriend);
            user.getFriends().add(userFriend);
        }
        log.info("Отправлен список друзей пользователя: {}", user);
        return new ResponseEntity<>(user.getFriends(), HttpStatus.valueOf(200));
    }

    public ResponseEntity deleteFriend(long userId, long friendId) throws EntityNotFoundException {
        final User user = userStorage.getUser(userId);
        final User userFriend = userStorage.getUser(friendId);

        SqlRowSet userRows =
                jdbcTemplate.queryForRowSet(
                        "SELECT * FROM FRIENDS WHERE USER_1 = ? AND USER_2 = ? AND STATUS = true ", userId, friendId);

        if (userRows.next()) {
            jdbcTemplate.update("DELETE FROM FRIENDS WHERE USER_1 = ? AND USER_2 = ? AND STATUS = true ", userId, friendId);
            jdbcTemplate.update("INSERT INTO FRIENDS (USER_1, USER_2, STATUS) values(?,?,?)", userId, friendId, false);
            log.info("У пользователя: {}, удален друг {} ", user, userFriend);
            return new ResponseEntity<>(user, HttpStatus.valueOf(200));
        }

        userRows =
                jdbcTemplate.queryForRowSet(
                        "SELECT * FROM FRIENDS WHERE USER_1 = ? AND USER_2 = ? AND STATUS = false ", userId, friendId);

        if (userRows.next()) {
            jdbcTemplate.update("DELETE FROM FRIENDS WHERE USER_1 = ? AND USER_2 = ? AND STATUS = false ", userId, friendId);
            log.info("У пользователя: {}, удален друг {} ", user, userFriend);
            return new ResponseEntity<>(user, HttpStatus.valueOf(200));
        }

        userRows =
                jdbcTemplate.queryForRowSet(
                        "SELECT * FROM FRIENDS WHERE USER_1 = ? AND USER_2 = ? AND STATUS = true ", friendId, userId);

        if (userRows.next()) {
            jdbcTemplate.update("UPDATE FRIENDS SET STATUS = false WHERE USER_1 = ? AND USER_2 = ? AND STATUS = true ", friendId, userId);
            log.info("У пользователя: {}, удален друг {} ", user, userFriend);
            return new ResponseEntity<>(user, HttpStatus.valueOf(200));
        }

        log.info("У пользователя: {}, нет друга {} ", user, userFriend);
        throw new EntityNotFoundException("Друг не найден");
    }

    public ResponseEntity getGeneralFriends(long id, long friendId) throws EntityNotFoundException {
        final User user = userStorage.getUser(id);
        final User userFriend = userStorage.getUser(friendId);
        final Set<User> friends = new HashSet<>();
        LinkedHashSet<User> listUser1 = (LinkedHashSet) getFriendsForId(id).getBody();
        LinkedHashSet<User> listUser2 = (LinkedHashSet) getFriendsForId(friendId).getBody();

        for (User userTest1 : listUser1) {
            for (User userTest2 : listUser2) {
                if (userTest1.getId() == userTest2.getId()) {
                    friends.add(userTest1);
                }
            }
        }
        log.info("Отправлен список общих друзей пользователей: {} и {} ", user, userFriend);
        return new ResponseEntity<>(friends, HttpStatus.valueOf(200));
    }

    public ResponseEntity userAdd(User user) {
        return userStorage.userAdd(user);
    }

    public ResponseEntity userGet(long id) throws EntityNotFoundException {
        final User user = userStorage.getUser(id);
        if (user != null) {
            log.info("Отправлен пользователь: {} ", user);
            return new ResponseEntity<>(user, HttpStatus.valueOf(200));
        }
        throw new EntityNotFoundException("Пользователь");
    }

    public ResponseEntity usersGet() throws EntityNotFoundException {
        log.info("Отправлен список всех пользователей");
        return new ResponseEntity<>(userStorage.usersGet(), HttpStatus.valueOf(200));
    }

    public ResponseEntity userUpdate(@Valid @RequestBody User user) {
        return userStorage.userUpdate(user);
    }
}