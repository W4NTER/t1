package ru.t1.exceptions;

public class CustomException extends RuntimeException {
    private ExceptionTypeEnum exceptionType;
    private String message;

    public CustomException(ExceptionTypeEnum type, String message) {
        this.exceptionType = type;
        this.message = message;
    }

    public CustomException(String message) {
        this.message = message;
    }

    public ExceptionTypeEnum getErrorType() {
        return exceptionType;
    }

    public String getMessage() {
        return message;
    }


}
