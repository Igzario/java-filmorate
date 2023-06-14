package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.FriendService;
import ru.yandex.practicum.filmorate.services.UserService;

import javax.validation.Valid;

@RequiredArgsConstructor
@Slf4j
@RestController
public class UserController {
    private final UserService userService;
    private final FriendService friendService;

    @PutMapping("/users/{id}/friends/{friendId}")
    public ResponseEntity addFriend(@PathVariable int id, @PathVariable int friendId) throws EntityNotFoundException {
        return friendService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public ResponseEntity deleteFriend(@PathVariable int id, @PathVariable int friendId) throws EntityNotFoundException {
        return friendService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public ResponseEntity getFriendsForId(@PathVariable int id) throws EntityNotFoundException {
        return friendService.getFriendsForId(id);
    }

    @GetMapping("/users/{id}/friends/common/{friendId}")
    public ResponseEntity getGeneralFriends(@PathVariable int id, @PathVariable int friendId) throws EntityNotFoundException {
        return friendService.getGeneralFriends(id, friendId);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity getUser(@PathVariable long id) throws EntityNotFoundException {
        return userService.getUser(id);
    }

    @GetMapping("/users")
    public ResponseEntity getUsers() throws EntityNotFoundException {
        return userService.getUsers();
    }

    @PostMapping("/users")
    public ResponseEntity addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping("/users")
    public ResponseEntity updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }
}