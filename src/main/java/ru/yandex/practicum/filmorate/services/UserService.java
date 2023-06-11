package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import javax.validation.Valid;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserService {
    private final UserDbStorage userDbStorage;

    public ResponseEntity addUser(User user) {
        return userDbStorage.addUser(user);
    }

    public ResponseEntity getUser(long id) throws EntityNotFoundException {
        final User user = userDbStorage.getUser(id);
        if (user != null) {
            log.info("Отправлен пользователь: {} ", user);
            return new ResponseEntity<>(user, HttpStatus.valueOf(200));
        }
        throw new EntityNotFoundException("Пользователь");
    }

    public ResponseEntity getUsers() throws EntityNotFoundException {
        log.info("Отправлен список всех пользователей");
        return new ResponseEntity<>(userDbStorage.getUsers(), HttpStatus.valueOf(200));
    }

    public ResponseEntity updateUser(@Valid @RequestBody User user) {
        return userDbStorage.updateUser(user);
    }
}