package ru.t1.dto.response;

public record TaskResponse(
        Long id,
        String title,
        String description,
        Long userId
) {
}
