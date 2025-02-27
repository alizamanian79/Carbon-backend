package com.gym.server.exception;

import org.springframework.http.HttpStatus;

public class AppException {
    private final HttpStatus httpStatus;
    private final String message;
    private final Throwable throwable;


    public AppException(String message, Throwable throwable, HttpStatus httpStatus) {
        this.message = message;
        this.throwable = throwable;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}