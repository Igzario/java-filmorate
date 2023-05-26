package ru.yandex.practicum.filmorate.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundExceptinon;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.UserService;
import javax.validation.Valid;

@Slf4j
@RestController
@Component
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public ResponseEntity addFriend(@PathVariable int id, @PathVariable int friendId) throws UserNotFoundExceptinon {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public ResponseEntity deleteFriend(@PathVariable int id, @PathVariable int friendId) throws UserNotFoundExceptinon {
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public ResponseEntity getFriendsForId(@PathVariable int id) throws UserNotFoundExceptinon {
        return userService.getFriendsForId(id);
    }

    @GetMapping("/users/{id}/friends/common/{friendId}")
    public ResponseEntity getGeneralFriends(@PathVariable int id, @PathVariable int friendId) throws UserNotFoundExceptinon {
        return userService.getGeneralFriends(id, friendId);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity userGet(@PathVariable long id) throws UserNotFoundExceptinon {
        return userService.userGet(id);
    }

    @GetMapping("/users")
    public ResponseEntity usersGet() {
        return userService.usersGet();
    }

    @PostMapping("/users")
    public ResponseEntity userAdd(@Valid @RequestBody User user) {
        return userService.userAdd(user);
    }

    @PutMapping("/users")
    public ResponseEntity userUpdate(@Valid @RequestBody User user) {
        return userService.userUpdate(user);
    }
}