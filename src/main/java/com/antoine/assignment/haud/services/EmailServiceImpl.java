package com.antoine.assignment.haud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Value("${email.subject}")
    private String subject;

    @Value("${email.textTemplate}")
    private String text;

    @Override
    public void sendSimpleMessage(String reveicer) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("testAntoine@test.com");
        message.setTo(reveicer);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}
