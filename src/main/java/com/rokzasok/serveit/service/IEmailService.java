package com.rokzasok.serveit.service;

import com.rokzasok.serveit.model.User;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Async;

import javax.mail.MessagingException;

public interface IEmailService {

    @Async
    void sendPasswordChangedEmail(String emailAddress, String username) throws MailException, InterruptedException, MessagingException;

    @Async
    void sendPasswordResetEmail(String emailAddress, String passwordHash) throws MailException, InterruptedException, MessagingException;
}
