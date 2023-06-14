package ru.yandex.practicum.filmorate.exception;

import java.sql.SQLException;

public class SqlException extends SQLException {

    public SqlException(String reason, String sqlState, int vendorCode) {
        super("ОШибка SQL" + reason, sqlState, vendorCode);
    }
}