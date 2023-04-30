package ru.yandex.practicum.filmorate.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.message.AppMessageError;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class UserController {
    List<User> users = new ArrayList<>();
    private int id = 1;

    @GetMapping("/users")
    public List<User> userTake() {
        return users;
    }

    @PostMapping("/users")
    public ResponseEntity userGive(@RequestBody User user) {
        try {
            if (!validation(user)) {
                throw new ValidationException();
            }
            user.setId(id);
            users.add(user);
            id++;
            log.info("Добавлен пользователь: {} ", user);
        } catch (Exception exception) {
            String error = exception.getMessage();
            if (exception.getClass() == ValidationException.class) {
                log.error(exception.getMessage());
                return new ResponseEntity<>(new AppMessageError(400,error), HttpStatus.BAD_REQUEST);
            } else {
                log.error("Не предвиденная ошибка: " + error);
                return new ResponseEntity<>(new AppMessageError(500,error), HttpStatus.valueOf(500));
            }
        }
        return new ResponseEntity(user, HttpStatus.valueOf(201));
    }


    @PutMapping("/users")
    public ResponseEntity userUpdate(@RequestBody User user) {
        try {
            if (!validation(user)) {
                throw new ValidationException();
            }
            for (User user1 : users) {
                if (user.getId() == user1.getId()) {
                    int i = users.indexOf(user1);
                    users.remove(user1);
                    users.add(i, user);
                    log.info("Обновлен пользователь: {} ", user);
                    return new ResponseEntity<>(user, HttpStatus.valueOf(200));
                }
            }
        } catch (Exception exception) {
            String error = exception.getMessage();
            if (exception.getClass() == ValidationException.class) {
                log.error(exception.getMessage());
                return new ResponseEntity<>(new AppMessageError(400,error), HttpStatus.BAD_REQUEST);
            } else {
                log.error("Не предвиденная ошибка: " + error);
                return new ResponseEntity<>(new AppMessageError(500,error), HttpStatus.valueOf(500));
            }
        }
        log.error("Не найден User для обновления");
        return new ResponseEntity<>("Не найден User для обновления", HttpStatus.valueOf(500));
    }

    private boolean validation(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            return false;
        } else if (user.getLogin() == null || user.getEmail().contains(" ")) {
            return false;
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            return false;
        }

        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        return true;
    }
}