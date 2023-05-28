package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class User {
    @JsonIgnore
    private final Set<User> friends = new LinkedHashSet<>();
    private int id;
    @NotEmpty(message = "Ошибка ввода - Email: null или empty")
    @Email(message = "Ошибка ввода - Email: not email format")
    private final String email;
    @Pattern(regexp = "[a-zA-Z0-9]{1,100}", message = "Ошибка ввода - login")
    private final String login;
    private String name;
    @PastOrPresent(message = "Ошибка ввода - birthday")
    private LocalDate birthday;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}