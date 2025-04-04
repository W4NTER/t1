package ru.t1.dto;

import ru.t1.exceptions.ExceptionTypeEnum;

public record CustomExceptionDTO(
        ExceptionTypeEnum exceptionType,
        String message) {

    public CustomExceptionDTO(String message) {
        this(ExceptionTypeEnum.ANY_EXCEPTION, message);
    }
}