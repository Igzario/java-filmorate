package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class UserController {
    final UserService userService = new UserService();

    @GetMapping("/users")
    public List<User> userTake() {
        return userService.getUsers();
    }

    @PostMapping("/users")
    public ResponseEntity userGive(@Valid @RequestBody User user) {
        return userService.userPost(user);
    }


    @PutMapping("/users")
    public ResponseEntity userUpdate(@Valid @RequestBody User user) {
        return userService.userPut(user);
    }
}