package com.pet.Bookshop.configuration.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;

    public void sendSimpleMessage(SimpleMailMessage message) {
        try {
            log.info("EmailServiceImpl-sendRegistrationMessage: Sending email to {}, topic: {}", message.getTo(), message.getSubject());
            emailSender.send(message);
        } catch (MailException exception) {
            log.error("EmailServiceImpl-sendRegistrationMessage: не получилось отправить письмо на почту {}, ошибка: {}", message.getTo(), exception.getMessage());

            //если не получилось отправить письмо то тут добавляем этот имейл в таблицу postponedEmail
            
            //и отправляем письмо позже тут что-то со scheduler

            throw exception;
        }

    }

}
