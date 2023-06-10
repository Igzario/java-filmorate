package ru.yandex.practicum.filmorate.exception;
import java.sql.SQLException;

public class SqlException extends SQLException {

    public SqlException(String reason, String SQLState, int vendorCode) {
        super("ОШибка SQL" + reason, SQLState, vendorCode);
    }
}
