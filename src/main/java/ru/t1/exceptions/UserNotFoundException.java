package ru.t1.exceptions;

public class UserNotFoundException extends CustomException {

    public UserNotFoundException(Long userId) {
        super(UserNotFoundException.class, "User with ID = " + userId + " not found");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
