package ru.yandex.practicum.filmorate.model;
import lombok.Data;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Data
public class Film {
    private int id;
    @NotBlank(message = "Ошибка ввода - name: пустое поле")
    private final String name;
    @Size(max = 200, min = 1, message = "Длина описания больше 200 символов")
    private final String description;
    private final LocalDate releaseDate;
    @Positive
    private final int duration;
    private final String mpa;
    private final List genres;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        if (name != null ? !name.equals(film.name) : film.name != null) return false;
        return releaseDate != null ? releaseDate.equals(film.releaseDate) : film.releaseDate == null;
    }
}