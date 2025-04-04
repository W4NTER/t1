package ru.t1.exceptions;

public class UserNotFoundException extends CustomException {

    public UserNotFoundException(Long userId) {
        super(ExceptionTypeEnum.USER_NOT_FOUND, "User with ID = " + userId + " not found");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
