package com.example.note2ubackendnosecurity.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String recipientAddress,
                         String subject,
                         String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("note2U@gmail.com");
        message.setTo(recipientAddress);
        message.setText(body);
        message.setSubject(subject);
        javaMailSender.send(message);


    }

}
