package org.example.Exceptions;

import java.sql.SQLException;

public class DaoException extends SQLException {
    public DaoException() {

    }

    public DaoException(String message) {
        super(message);
    }
}