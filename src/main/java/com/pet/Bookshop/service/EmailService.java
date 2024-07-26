package com.pet.Bookshop.service;

import com.pet.Bookshop.dto.EmailDto;
import com.pet.Bookshop.dto.SignUpDto;
import com.pet.Bookshop.security.UUIDGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;
    private final MailProperties mailProperties;
    private final UUIDGenerator uuidGenerator;

    public void sendSimpleMessage(SimpleMailMessage message) {
        try {
            log.info("EmailService-sendSimpleMessage: Sending email to {}, subject: {}", message.getTo(), message.getSubject());
            emailSender.send(message);
        } catch (MailException exception) {
            log.error("EmailService-sendSimpleMessage: Failed to send email to {}, error: {}", message.getTo(), exception.getMessage());
            throw exception;
        }
    }

    public SimpleMailMessage createRegistrationMessage(SignUpDto signUpDto) {
        String defaultRegistrationText = mailProperties.getProperties().get("registration-text");
        UUID uuid = uuidGenerator.generateUUIDFromValue(signUpDto.getLogin());
        String formattedString = MessageFormat.format(defaultRegistrationText, signUpDto.getLogin(), uuid);

        log.info("EmailService-createRegistrationMessage: preparing email for: {}", signUpDto.getEmail());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailProperties.getUsername());
        message.setTo(signUpDto.getEmail());
        message.setSubject("Registration");
        message.setText(formattedString);

        return message;
    }

    public String sendAdminEmail(EmailDto emailDto) {
        SimpleMailMessage message = createAdminMessage(emailDto);
        sendSimpleMessage(message);
        return MessageFormat.format("Email successfully sent to recipient: {0}", emailDto.getTo());
    }

    private SimpleMailMessage createAdminMessage(EmailDto emailDto) {
        log.info("EmailService-createAdminMessage: preparing ADMIN email for: {}", emailDto.getTo());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailProperties.getUsername());
        message.setTo(emailDto.getTo());
        message.setSubject(emailDto.getSubject());
        message.setText(emailDto.getText());

        return message;
    }
}