package com.pet.Bookshop.configuration.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/send")
@RequiredArgsConstructor
public class EmailController {
    @Autowired
    private final MailUtil mailUtil;
    private final EmailService emailService;

    @PostMapping("/registrationEmail")
    public void sendRegistrationEmail(String to) {
        SimpleMailMessage message = mailUtil.createMessage("Registration", EmailAction.REGISTRATION, to);
        emailService.sendSimpleMessage(message);
    }

}
