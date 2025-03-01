package com.gym.server.exception;

public class AppForbiddenException extends RuntimeException {

    public AppForbiddenException(String message) {
        super(message);
    }

    public AppForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}


