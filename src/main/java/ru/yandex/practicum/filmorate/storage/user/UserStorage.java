package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    ResponseEntity addUser(User user);

    List<User> getUsers() throws EntityNotFoundException;

    ResponseEntity updateUser(User user);

    User getUser(long id) throws EntityNotFoundException;
}