package com.pet.Bookshop.service;

import com.pet.Bookshop.dto.EmailDto;
import com.pet.Bookshop.dto.SignUpDto;
import com.pet.Bookshop.security.UUIDGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @Mock
    private MailProperties mailProperties;
    @Mock
    private UUIDGenerator uuidGenerator;
    @Mock
    private JavaMailSender emailSender;
    @InjectMocks
    private EmailService emailService;

    @Test
    void testCreateRegistrationMessage() {
        // Создание тестовых данных
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setLogin("testuser");
        signUpDto.setEmail("testuser@example.com");

        // Установка поведения для mock объекта mailProperties
        when(mailProperties.getProperties()).thenReturn(Collections.singletonMap("registration-text", "Hello, {0}. Welcome to our platform!"));

        // Вызов тестируемого метода
        SimpleMailMessage message = emailService.createRegistrationMessage(signUpDto);

        // Проверка результатов
        assertEquals(mailProperties.getUsername(), message.getFrom());
        assertEquals("Registration", message.getSubject());
        assertEquals("Hello, testuser. Welcome to our platform!", message.getText());
    }

//    @Test
//    void testCreateAdminMessage() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        // Создание тестовых данных
//        EmailDto emailDto = new EmailDto();
//        emailDto.setTo("to@example.com");
//        emailDto.setSubject("Test Subject");
//        emailDto.setText("Test message body");
//
//        // Установка поведения
//        when(mailProperties.getUsername()).thenReturn("from@example.com");
//
//        // добираемся до приватного метода
//        Method createAdminMessageMethod = EmailService.class.getDeclaredMethod("createAdminMessage", EmailDto.class);
//        createAdminMessageMethod.setAccessible(true);
//
//        // Вызов метода
//        SimpleMailMessage message = (SimpleMailMessage) createAdminMessageMethod.invoke(emailService, emailDto);
//
//        // Проверка результатов
//        assertEquals("from@example.com", message.getFrom());
//        assertEquals("Test Subject", message.getSubject());
//        assertEquals("Test message body", message.getText());
//    }

    @Test
    void testSendSimpleMessage() {
        // Подготовка тестовых данных
        SimpleMailMessage testMessage = new SimpleMailMessage();
        testMessage.setTo("recipient@example.com");
        testMessage.setSubject("Test Subject");
        testMessage.setText("Test message body");

        // Установка поведения
        doNothing().when(emailSender).send(testMessage);

        // Вызов метода
        assertDoesNotThrow(() -> emailService.sendSimpleMessage(testMessage));

        // Проверка вызова метода
        verify(emailSender, times(1)).send(testMessage);
    }

    @Test
    void testSendAdminEmail() {
        // Подготовка тестовых данных
        EmailDto testEmailDto = new EmailDto();
        testEmailDto.setTo("admin@example.com");
        testEmailDto.setSubject("Admin Test Subject");
        testEmailDto.setText("Admin Test message body");

        // Установка поведения
        when(mailProperties.getUsername()).thenReturn("from@example.com");

        SimpleMailMessage testMessage = new SimpleMailMessage();
        testMessage.setTo(testEmailDto.getTo());
        testMessage.setSubject(testEmailDto.getSubject());
        testMessage.setText(testEmailDto.getText());

        // Установка поведения JavaMailSender mock объекта
        doNothing().when(emailSender).send(any(SimpleMailMessage.class));

        // Вызов тестируемого метода
        String result = emailService.sendAdminEmail(testEmailDto);

        // Проверка результата
        assertEquals("Email successfully sent to recipient: admin@example.com", result);

    }

    @Test
    void testCreateAdminMessage() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final var emailDto = new EmailDto("to@example.com", "Test Subject", "Test actualMessage body");
        final var expectedMessage =  new SimpleMailMessage();
        expectedMessage.setFrom("from@example.com");
        expectedMessage.setTo(emailDto.getTo());
        expectedMessage.setSubject(emailDto.getSubject());
        expectedMessage.setText(emailDto.getText());

        when(mailProperties.getUsername()).thenReturn("from@example.com");

        // добираемся до приватного метода
        Method createAdminMessageMethod = EmailService.class.getDeclaredMethod("createAdminMessage", EmailDto.class);
        createAdminMessageMethod.setAccessible(true);

        // Вызов метода
        final var actualMessage = (SimpleMailMessage) createAdminMessageMethod.invoke(emailService, emailDto);

        // Проверка результатов
        assertEquals(expectedMessage.getFrom(), actualMessage.getFrom());
        assertEquals(expectedMessage.getSubject(), actualMessage.getSubject());
        assertEquals(expectedMessage.getText(), actualMessage.getText());
    }


}
