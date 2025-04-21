package ru.t1.dto;

public record CustomExceptionDto(
        Class<? extends Exception> exceptionType,
        String message) {
}