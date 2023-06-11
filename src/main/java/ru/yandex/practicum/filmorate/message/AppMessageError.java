package ru.yandex.practicum.filmorate.message;
import lombok.Getter;
import lombok.Setter;

public class AppMessageError {
    @Getter @Setter
    private int statusCode;
    @Getter @Setter
    private String message;

    public AppMessageError(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public AppMessageError() {
    }
}
