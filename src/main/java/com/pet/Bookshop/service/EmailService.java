package com.pet.Bookshop.service;

import com.pet.Bookshop.dto.EmailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;
    private final MailService mailService;

    public void sendSimpleMessage(SimpleMailMessage message) {
        try {
            log.info("EmailServiceImpl-sendSimpleMessage: Sending email to {}, topic: {}", message.getTo(), message.getSubject());
            emailSender.send(message);
        } catch (MailException exception) {
            log.error("EmailServiceImpl-sendSimpleMessage: не получилось отправить письмо на почту {}, ошибка: {}", message.getTo(), exception.getMessage());

            //если не получилось отправить письмо то тут добавляем этот имейл в таблицу postponedEmail

            //и отправляем письмо позже тут что-то со scheduler

            throw exception;
        }

    }


    public String sendAdminEmail(EmailDto emailDto) {
        SimpleMailMessage message = mailService.createAdminMessage(emailDto);
        sendSimpleMessage(message);
        return MessageFormat.format("Письмо успешно отправлено получателю: {0}", emailDto.getTo());
    }
}
