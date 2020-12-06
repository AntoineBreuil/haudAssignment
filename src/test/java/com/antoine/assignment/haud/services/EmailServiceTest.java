package com.antoine.assignment.haud.services;

import com.antoine.assignment.haud.HaudApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = HaudApplication.class)
@TestPropertySource("/test.properties")
public class EmailServiceTest {

    @Autowired
    private EmailServiceImpl emailServiceImpl;

    @MockBean
    private JavaMailSender emailsender;

    @Test
    public void sendSimpleMessageTest() {

        String receiver = "testReceiver@gmail.com";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("testAntoine@test.com");
        message.setTo(receiver);
        message.setSubject("testSubject");
        message.setText("testTemplate");

        emailServiceImpl.sendSimpleMessage(receiver);
        Mockito.verify(emailsender, Mockito.times(1)).send(eq(message));
    }

}
