package com.pet.Bookshop.utils;

import com.pet.Bookshop.model.dto.EmailDto;
import com.pet.Bookshop.model.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailUtil {
    private final MailProperties mailProperties;

    public SimpleMailMessage createRegistrationMessage(SignUpDto signUpDto) {

        String defaultRegistrationText = mailProperties.getProperties().get("registration-text");
        String formattedString = MessageFormat.format(defaultRegistrationText, signUpDto.getLogin());

        log.info("mailUtil-createSimpleMessage: preparing email for: {}", signUpDto.getEmail());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailProperties.getUsername()); //от кого всегда одинаково
        message.setTo(signUpDto.getEmail());
        message.setSubject("Registration");
        message.setText(formattedString);

        return message;
    }


    public SimpleMailMessage createAdminMessage(EmailDto emailDto) {

        log.info("mailUtil-createSimpleMessage: preparing ADMIN email for: {}", emailDto.getTo());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailProperties.getUsername()); //от кого всегда одинаково
        message.setTo(emailDto.getTo());
        message.setSubject(emailDto.getSubject());
        message.setText(emailDto.getText());

        return message;
    }


}
