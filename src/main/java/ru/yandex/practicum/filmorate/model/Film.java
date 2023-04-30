package ru.yandex.practicum.filmorate.model;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    private int id;
    @NotNull(message = "Ошибка ввода - name")
    @NotBlank(message = "Ошибка ввода - name")
    private final String name;
    @NotNull(message = "Ошибка ввода - description")
    @NotBlank(message = "Ошибка ввода - descriptio")
    private final String description;
    @NotNull(message = "Ошибка ввода - releaseDate")
    private final LocalDate releaseDate;
    @NotNull(message = "Ошибка ввода - duration")
    private final Duration duration;

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
