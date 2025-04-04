package ru.t1.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.t1.dto.CustomExceptionDTO;
import ru.t1.exceptions.EntityAlreadyExistsException;
import ru.t1.exceptions.EntityNotFoundException;
import ru.t1.exceptions.LoginAlreadyInUseException;
import ru.t1.exceptions.UserNotFoundException;

@ControllerAdvice
public class ExceptionControllerAdvice {
    private static final Logger LOGGER = LogManager.getLogger();

    @ExceptionHandler(java.lang.Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CustomExceptionDTO exception(java.lang.Exception e) {
        LOGGER.error(e.getMessage());
        return new CustomExceptionDTO(e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomExceptionDTO entityNotFound(EntityNotFoundException e) {
        LOGGER.error(e.getMessage());
        return new CustomExceptionDTO(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomExceptionDTO userNotFound(UserNotFoundException e) {
        LOGGER.error(String.format("Exception type - (%s), Message - %s", e.getErrorType(), e.getMessage()));
        return new CustomExceptionDTO(e.getErrorType(), "Пользователь не найден");
    }

    @ExceptionHandler(LoginAlreadyInUseException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public CustomExceptionDTO conflictUser(LoginAlreadyInUseException e) {
        LOGGER.error(String.format("Exception type - (%s), Message - %s", e.getErrorType(), e.getMessage()));
        return new CustomExceptionDTO(e.getErrorType(), "Username уже испльзуется другим пользователем");
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public CustomExceptionDTO entityAlreadyExists(EntityAlreadyExistsException e) {
        LOGGER.error(String.format("Exception type - (%s), Message - %s", e.getErrorType(), e.getMessage()));
        return new CustomExceptionDTO(e.getErrorType(), e.getMessage());
    }
}
