package com.gym.server.exception;

public class AppBadRequest extends RuntimeException {

    public AppBadRequest(String message) {
        super(message);
    }

    public AppBadRequest(String message, Throwable cause) {
        super(message, cause);
    }
}