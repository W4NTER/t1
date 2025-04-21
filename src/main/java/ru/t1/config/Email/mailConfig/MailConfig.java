package ru.t1.config.Email.mailConfig;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.mail")
public record MailConfig(
        @NotNull
        String senderLogin,

        @NotNull
        String senderPassword,

        @NotNull
        Integer port,

        @NotNull
        String subject
) {
}
