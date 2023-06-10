package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    ResponseEntity userAdd(User user);

    List<User> usersGet() throws EntityNotFoundException;

    ResponseEntity userUpdate(User user);

    User getUser(long id) throws EntityNotFoundException;
}