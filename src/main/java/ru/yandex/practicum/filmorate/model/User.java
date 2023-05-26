package ru.yandex.practicum.filmorate.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    @JsonIgnore
    private final Set<User> friends = new HashSet<>();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        return login != null ? login.equals(user.login) : user.login == null;
    }
}