package com.gym.server.exception;

public class AppExistException extends RuntimeException {

    public AppExistException(String message) {
        super(message);
    }

    public AppExistException(String message, Throwable cause) {
        super(message, cause);
    }
}