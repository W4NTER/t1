package ru.t1.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public record UserRequest(
        String login,
        String password,
        String email
) {
}
