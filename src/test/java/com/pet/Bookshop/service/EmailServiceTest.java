package com.pet.Bookshop.service;

import com.pet.Bookshop.dto.EmailDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
    @Mock
    private JavaMailSender emailSender; //No ParameterResolver registered for parameter
    @Mock
    private MailService mailService;
    @InjectMocks
    private EmailService emailService;

    @Test
    public void testSendSimpleMessage_Success() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("victoryagraz@example.com");
        message.setSubject("Test subject");

        when(mailService.createAdminMessage(any())).thenReturn(message);

        emailService.sendAdminEmail(new EmailDto("victoryagraz@example.com", "Test subject", "Test message"));

        verify(emailSender, times(1)).send(message);
    }

    @Test
    public void testSendSimpleMessage_Failure() {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("victoryagraz@example.com");
        message.setSubject("Test subject");

        when(mailService.createAdminMessage(any())).thenReturn(message);

        doThrow(new MailSendException("Симуляция отправки письма не получилась")).when(emailSender).send(message);

        assertThrows(MailSendException.class, () -> emailService.sendAdminEmail(new EmailDto("receiver@example.com", "Test subject", "Test message")));
    }


}
