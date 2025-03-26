package com.gym.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = {AppNotFoundException.class})
    public ResponseEntity<Object> handleAppNotFoundException
            (AppNotFoundException appNotFoundException)
    {
        AppException appException = new AppException(
                appNotFoundException.getMessage(),
                appNotFoundException.getCause(),
                HttpStatus.NOT_FOUND
        );

        return new ResponseEntity<>(appException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {AppExistException.class})
    public ResponseEntity<Object> handleAppExistException
            (AppExistException appExistException)
    {
        AppException appException = new AppException(
                appExistException.getMessage(),
                appExistException.getCause(),
                HttpStatus.CONFLICT
        );

        return new ResponseEntity<>(appException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {AppForbiddenException.class})
    public ResponseEntity<Object> handleAppForbiddenException
            (AppForbiddenException appForbiddenException)
    {
        AppException appException = new AppException(
                appForbiddenException.getMessage(),
                appForbiddenException.getCause(),
                HttpStatus.FORBIDDEN
        );

        return new ResponseEntity<>(appException, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {AppSuccessfullException.class})
    public ResponseEntity<Object> handleAppSuccessfullException
            (AppSuccessfullException appSuccessfullException)
    {
        AppException appException = new AppException(
                appSuccessfullException.getMessage(),
                appSuccessfullException.getCause(),
                HttpStatus.OK
        );

        return new ResponseEntity<>(appException, HttpStatus.OK);
    }



    @ExceptionHandler(value = {AppBadRequest.class})
    public ResponseEntity<Object> handleAppBadRequestException
            (AppBadRequest appBadRequest)
    {
        AppException appException = new AppException(
                appBadRequest.getMessage(),
                appBadRequest.getCause(),
                HttpStatus.BAD_REQUEST
        );

        return new ResponseEntity<>(appException, HttpStatus.BAD_REQUEST);
    }

}