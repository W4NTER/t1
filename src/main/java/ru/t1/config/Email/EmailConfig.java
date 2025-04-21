package ru.t1.config.Email;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import ru.t1.config.Email.mailConfig.MailConfig;

import java.util.Properties;

@Configuration
@EnableConfigurationProperties(MailConfig.class)
public class EmailConfig {
    private final MailConfig mail;
    private final static String HOST = "smtp.mail.ru";

    public EmailConfig(MailConfig mail) {
        this.mail = mail;
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(HOST);
        mailSender.setPort(mail.port());

        mailSender.setUsername(mail.senderLogin());
        mailSender.setPassword(mail.senderPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "false");
        props.put("mail.debug", "true");
        props.put("mail.smtp.ssl.enable", "true");

        return mailSender;
    }
}
