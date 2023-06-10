package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.UserService;

import javax.validation.Valid;

@RequiredArgsConstructor
@Slf4j
@RestController
@Component
public class UserController {

    private final UserService userService;

    @PutMapping("/users/{id}/friends/{friendId}")
    public ResponseEntity addFriend(@PathVariable int id, @PathVariable int friendId) throws EntityNotFoundException {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public ResponseEntity deleteFriend(@PathVariable int id, @PathVariable int friendId) throws EntityNotFoundException {
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public ResponseEntity getFriendsForId(@PathVariable int id) throws EntityNotFoundException {
        return userService.getFriendsForId(id);
    }

    @GetMapping("/users/{id}/friends/common/{friendId}")
    public ResponseEntity getGeneralFriends(@PathVariable int id, @PathVariable int friendId) throws EntityNotFoundException {
        return userService.getGeneralFriends(id, friendId);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity getUser(@PathVariable long id) throws EntityNotFoundException {
        return userService.userGet(id);
    }

    @GetMapping("/users")
    public ResponseEntity getUsers() throws EntityNotFoundException {
        return userService.usersGet();
    }

    @PostMapping("/users")
    public ResponseEntity addUser(@Valid @RequestBody User user) {
        return userService.userAdd(user);
    }

    @PutMapping("/users")
    public ResponseEntity updateUser(@Valid @RequestBody User user) {
        return userService.userUpdate(user);
    }
}