package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final List<User> users = new ArrayList<>();
    private int id = 1;

    @Override
    public ResponseEntity userAdd(User user) {
        user.setId(id);
        validName(user);
        users.add(user);
        id++;
        log.info("Добавлен пользователь: {} ", user);
        return new ResponseEntity(user, HttpStatus.valueOf(201));
    }

    @Override
    public List<User> usersGet() {
        return users;
    }

    @Override
    public ResponseEntity userRefresh(User user) {
        validName(user);
        for (User user1 : users) {
            if (user.getId() == user1.getId()) {
                int i = users.indexOf(user1);
                users.remove(user1);
                users.add(i, user);
                log.info("Обновлен пользователь: {} ", user);
                return new ResponseEntity<>(user, HttpStatus.valueOf(200));
            }
        }
        log.error("Не найден User для обновления");
        return new ResponseEntity<>(user, HttpStatus.valueOf(500));
    }

    @Override
    public User getUser(long id) {
        User user = null;
        for (User findUser : users) {
            if (findUser.getId() == id) {
                user = findUser;
                break;
            }
        }
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