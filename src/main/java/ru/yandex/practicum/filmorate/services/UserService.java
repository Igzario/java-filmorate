package ru.yandex.practicum.filmorate.services;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.UserNotFoundExceptinon;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class UserService {
    private final UserStorage inMemoryUserStorage;

    @Autowired
    public UserService(UserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public ResponseEntity addFriend(long id, long friendId) throws UserNotFoundExceptinon {
        final User user = inMemoryUserStorage.getUser(id);
        final User userFriend = inMemoryUserStorage.getUser(friendId);
        if (user != null && userFriend != null) {
            log.info("Пользователю: {}, добавлен друг {} ", user, userFriend);
            user.getFriends().add(userFriend);
            userFriend.getFriends().add(user);
        } else {
            throw new UserNotFoundExceptinon();
        }
        return new ResponseEntity<>(user, HttpStatus.valueOf(200));
    }

    public ResponseEntity deleteFriend(long id, long friendId) throws UserNotFoundExceptinon {
        final User user = inMemoryUserStorage.getUser(id);
        final User userFriend = inMemoryUserStorage.getUser(friendId);
        if (user != null && userFriend != null) {
            log.info("У пользователя: {}, удален друг {} ", user, userFriend);
            user.getFriends().remove(friendId);
            userFriend.getFriends().remove(id);
        } else {
            throw new UserNotFoundExceptinon();
        }
        return new ResponseEntity<>(user, HttpStatus.valueOf(204));
    }

    public ResponseEntity getFriendsForId(long id) throws UserNotFoundExceptinon {
        final User user = inMemoryUserStorage.getUser(id);
        if (user != null) {
            log.info("Отправлен список друзей пользователя: {}", user);
            return new ResponseEntity<>(user.getFriends(), HttpStatus.valueOf(200));
        } else {
            throw new UserNotFoundExceptinon();
        }
    }

    public ResponseEntity getGeneralFriends(long id, long friendId) throws UserNotFoundExceptinon {
        final User user = inMemoryUserStorage.getUser(id);
        final User userFriend = inMemoryUserStorage.getUser(friendId);
        if (user != null && userFriend != null) {
            log.info("Отправлен список общих друзей пользователей: {} и {} ", user, userFriend);
            final Set<User> friends = new HashSet<>();
            for (User userFind : user.getFriends()) {
                if (userFriend.getFriends().contains(userFind)) {
                    friends.add(userFind);
                }
            }
            return new ResponseEntity<>(friends, HttpStatus.valueOf(200));
        } else {
            throw new UserNotFoundExceptinon();
        }
    }

    public ResponseEntity userAdd(User user) {
        return inMemoryUserStorage.userAdd(user);
    }

    public ResponseEntity userGet(long id) throws UserNotFoundExceptinon {
        final User user = inMemoryUserStorage.getUser(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.valueOf(200));
        }
        throw new UserNotFoundExceptinon();
    }

    public ResponseEntity usersGet() {
        return new ResponseEntity<>(inMemoryUserStorage.usersGet(), HttpStatus.valueOf(200));
    }

    public ResponseEntity userUpdate(@Valid @RequestBody User user) {
        return inMemoryUserStorage.userRefresh(user);
    }
}