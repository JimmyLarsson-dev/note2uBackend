package com.example.note2ubackendnosecurity.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Value("${spring.mail.username}") private String from;

    public String sendMail(EmailDetails emailDetails) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(emailDetails.getRecipient());
            message.setText(emailDetails.getMsgBody());
            message.setSubject(emailDetails.getSubject());
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            return "failed";
        }
        return "success";
    }
}
