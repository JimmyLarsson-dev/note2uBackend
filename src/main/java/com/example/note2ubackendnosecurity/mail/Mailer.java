package com.example.note2ubackendnosecurity.mail;

import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.UUID;

public class Mailer {



    public Mailer() {
    }

    public String sendEmail(String email) {
        String resetToken = UUID.randomUUID().toString();
        EmailDetails emailDetails = new EmailDetails(email, "this is the text, hardcoded in authService, replace it, please", "temporary testmail",  "attachment");
        EmailService emailService = new EmailServiceImpl(new JavaMailSenderImpl());
        emailService.sendMail(emailDetails);
        return resetToken;
    }

}
