package ru.t1.mailer.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ru.starter_t1.aspect.annotation.LogException;
import ru.t1.config.Email.mailConfig.MailConfig;
import ru.t1.mailer.MailSender;

@Component
public class MailSenderImpl implements MailSender {
    private final JavaMailSender javaMailSender;
    private final MailConfig mail;

    public MailSenderImpl(
            JavaMailSender javaMailSender,
            MailConfig mail) {
        this.javaMailSender = javaMailSender;
        this.mail = mail;
    }

    @LogException
    @Override
    public void sendMail(String toAddress, String content) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(mail.senderLogin());
        simpleMailMessage.setTo(toAddress);
        simpleMailMessage.setSubject(mail.subject());
        simpleMailMessage.setText(content);

        javaMailSender.send(simpleMailMessage);
    }
}
