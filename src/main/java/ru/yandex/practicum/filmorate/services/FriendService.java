package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendService {
    private final UserDbStorage userDbStorage;
    private final JdbcTemplate jdbcTemplate;

    public ResponseEntity addFriend(long userId, long friendId) throws EntityNotFoundException {
        final User user = userDbStorage.getUser(userId);
        final User userFriend = userDbStorage.getUser(friendId);
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
        final User user = userDbStorage.getUser(id);
        final Set<User> friends = new LinkedHashSet<>();
        int idFriend;
        SqlRowSet userRows =
                jdbcTemplate.queryForRowSet(
                        "select * from FRIENDS WHERE USER_1 = ?", id);
        while (userRows.next()) {
            idFriend = Integer.parseInt(userRows.getString("USER_2"));
            User userFriend = userDbStorage.getUser(idFriend);
            friends.add(userFriend);
        }
        userRows =
                jdbcTemplate.queryForRowSet(
                        "select * from FRIENDS WHERE USER_2 = ? AND STATUS = true", id);
        while (userRows.next()) {
            idFriend = Integer.parseInt(userRows.getString("USER_1"));
            User userFriend = userDbStorage.getUser(idFriend);
            friends.add(userFriend);
        }
        log.info("Отправлен список друзей пользователя: {}", user);
        return new ResponseEntity<>(friends, HttpStatus.valueOf(200));
    }

    public ResponseEntity deleteFriend(long userId, long friendId) throws EntityNotFoundException {
        final User user = userDbStorage.getUser(userId);
        final User userFriend = userDbStorage.getUser(friendId);

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
        final User user = userDbStorage.getUser(id);
        final User userFriend = userDbStorage.getUser(friendId);
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
}