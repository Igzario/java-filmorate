package ru.yandex.practicum.filmorate.services;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.message.AppMessageError;
import ru.yandex.practicum.filmorate.model.User;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserService {
    @Getter
    private final List<User> users = new ArrayList<>();
    private int id = 1;

    public ResponseEntity userPost(User user) {
        try {
            user.setId(id);
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            users.add(user);
            id++;
            log.info("Добавлен пользователь: {} ", user);
        } catch (Exception exception) {
            String error = exception.getMessage();
            log.error("Не предвиденная ошибка: " + error);
            return new ResponseEntity<>(new AppMessageError(500, error), HttpStatus.valueOf(500));
        }
        return new ResponseEntity(user, HttpStatus.valueOf(201));
    }

    public ResponseEntity userPut(User user) {
        try {
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
            log.error("Не предвиденная ошибка: " + error);
            return new ResponseEntity<>(new AppMessageError(500, error), HttpStatus.valueOf(500));
        }
        log.error("Не найден User для обновления");
        return new ResponseEntity<>(user, HttpStatus.valueOf(500));
    }
}
