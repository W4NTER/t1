package ru.t1.dto.response;

public record UserResponse(
        Long id,
        String login,
        String password
) {
}
