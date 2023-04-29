package ru.yandex.practicum.filmorate.model;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class User {
    private int id;
    @Email(message = "Ошибка ввода - Email, попробуйте еще раз")
    private final String email;
    private final String login;
    private String name;
    @NotNull(message = "Ошибка ввода - birthday, попробуйте еще раз")
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