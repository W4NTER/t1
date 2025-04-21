package ru.t1.exceptions;


public class LoginAlreadyInUseException extends CustomException {

    public LoginAlreadyInUseException(String username) {
        super(LoginAlreadyInUseException.class,
                String.format("Пользователь с таким username = %s, уже существует", username));
    }
}
