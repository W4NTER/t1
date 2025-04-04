package ru.t1.exceptions;


public class LoginAlreadyInUseException extends CustomException {

    public LoginAlreadyInUseException(String username) {
        super(ExceptionTypeEnum.LOGIN_ALREADY_IN_USE,
                String.format("Пользователь с таким username = %s, уже существует", username));
    }
}
