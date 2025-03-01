package com.gym.server.exception;

public class AppSuccessfullException extends RuntimeException {

    public AppSuccessfullException(String message) {
        super(message);
    }

    public AppSuccessfullException(String message, Throwable cause) {
        super(message, cause);
    }
}