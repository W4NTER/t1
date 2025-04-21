package ru.t1.exceptions;

public class InvalidEmailException extends CustomException{
    public InvalidEmailException(String email) {
        super(InvalidEmailException.class, String.format("Email - %s has not valid format", email));
    }
}
