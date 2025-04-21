package ru.t1.mailer;

import java.util.concurrent.TimeoutException;

public interface MailSender {
    void sendMail (String toAddress, String content);
}
