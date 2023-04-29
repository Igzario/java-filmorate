package ru.yandex.practicum.filmorate.message;
import lombok.Getter;

public class AppMessageOk extends AppMessageError {
    @Getter
    private Object object;

    public AppMessageOk(int statusCode, String message, Object obj) {
        this.setStatusCode(statusCode);
        this.setMessage(message);
        this.object = obj;
    }
}
