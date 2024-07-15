package com.pet.Bookshop.service;

import com.pet.Bookshop.dto.EmailDto;
import com.pet.Bookshop.dto.SignUpDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MailServiceTest {
    @Mock
    private MailProperties mailProperties;
    @InjectMocks
    private MailService mailService;

    @Test
    void testCreateRegistrationMessage() {
        // Создание тестовых данных
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setLogin("testuser");
        signUpDto.setEmail("testuser@example.com");

        // Установка поведения для mock объекта mailProperties
        when(mailProperties.getProperties()).thenReturn(Collections.singletonMap("registration-text", "Hello, {0}. Welcome to our platform!"));

        // Вызов тестируемого метода
        SimpleMailMessage message = mailService.createRegistrationMessage(signUpDto);

        // Проверка результатов
        assertEquals(mailProperties.getUsername(), message.getFrom());
        assertEquals("Registration", message.getSubject());
        assertEquals("Hello, testuser. Welcome to our platform!", message.getText());
    }

    @Test
    void testCreateAdminMessage() {
        // Создание тестовых данных
        EmailDto emailDto = new EmailDto();
        emailDto.setTo("to@example.com");
        emailDto.setSubject("Test Subject");
        emailDto.setText("Test message body");

        // Установка поведения для mock объекта mailProperties
        when(mailProperties.getUsername()).thenReturn("from@example.com");

        // Вызов тестируемого метода
        SimpleMailMessage message = mailService.createAdminMessage(emailDto);

        // Проверка результатов
        assertEquals("from@example.com", message.getFrom());
        assertEquals("Test Subject", message.getSubject());
        assertEquals("Test message body", message.getText());
    }

}
