package ru.t1.dto.request;

public record UserRequest(
        String login,
        String password
) {
}
