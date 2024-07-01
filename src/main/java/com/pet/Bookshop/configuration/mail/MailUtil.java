package com.pet.Bookshop.configuration.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailUtil {
    private final MailProperties mailProperties;

    public SimpleMailMessage createMessage(String subject, EmailAction action, String to) {
        log.info("mailUtil-createSimpleMessage: preparing email: \nsubject {}\n text {}", subject, mailProperties.getProperties().get("registration-text"));
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailProperties.getUsername()); //от кого всегда одинаково
        message.setTo(to);
        message.setSubject(subject);
        if(action.equals(EmailAction.REGISTRATION)){
            message.setText( mailProperties.getProperties().get("registration-text"));
        }
        //else...

        return message;
    }


}
