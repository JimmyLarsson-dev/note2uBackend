package com.example.note2ubackendnosecurity.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public interface EmailService {

   String sendMail(EmailDetails emailDetails);

}
