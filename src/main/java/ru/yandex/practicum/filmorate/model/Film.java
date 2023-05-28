package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;

@Data
public class Film {
    private final HashMap<Integer, String> likes = new HashMap();
    private int id;
    @NotBlank(message = "Ошибка ввода - name: пустое поле")
    private final String name;
    @Size(max = 200, min = 1, message = "Длина описания больше 200 символов")
    private final String description;
    private final LocalDate releaseDate;
    @Positive
    private final int duration;

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", releaseDate=" + releaseDate +
                ", duration=" + duration +
                '}';
    }
}