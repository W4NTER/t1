package ru.t1.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.t1.dto.CustomExceptionDto;
import ru.t1.exceptions.*;

@ControllerAdvice
public class ExceptionControllerAdvice {
    private static final Logger LOGGER = LogManager.getLogger();

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CustomExceptionDto exception(java.lang.Exception e) {
        LOGGER.error("message - {}, StackTrace - {}", e.getMessage(), e.getStackTrace());
        return new CustomExceptionDto(e.getClass(), e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomExceptionDto handleInvalidJson(HttpMessageNotReadableException e) {
        LOGGER.error("Invalid JSON input: {}, StackTrace - {}", e.getMessage(), e.getStackTrace());
        return new CustomExceptionDto(e.getClass(), "Invalid JSON format: " + e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomExceptionDto entityNotFound(EntityNotFoundException e) {
        return new CustomExceptionDto(e.getClass(), e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomExceptionDto userNotFound(UserNotFoundException e) {
        return new CustomExceptionDto(e.getClass(), e.getMessage());
    }

    @ExceptionHandler(LoginAlreadyInUseException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public CustomExceptionDto conflictUser(LoginAlreadyInUseException e) {
        return new CustomExceptionDto(e.getClass(), e.getMessage());
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public CustomExceptionDto entityAlreadyExists(EntityAlreadyExistsException e) {
        return new CustomExceptionDto(e.getClass(), e.getMessage());
    }

    @ExceptionHandler(InvalidEmailException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomExceptionDto invalidEmail(InvalidEmailException e) {
        return new CustomExceptionDto(e.getClass(), e.getMessage());
    }
}
