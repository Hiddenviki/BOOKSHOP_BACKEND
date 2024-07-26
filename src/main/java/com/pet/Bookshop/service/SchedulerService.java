package com.pet.Bookshop.service;

import com.pet.Bookshop.dto.SignUpDto;
import com.pet.Bookshop.entity.PostponedEmail;
import com.pet.Bookshop.entity.User;
import com.pet.Bookshop.repository.PostponedEmailRepository;
import com.pet.Bookshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
@RequiredArgsConstructor
public class SchedulerService {
    private final PostponedEmailRepository postponedEmailRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    public void checkPostponedEmails() {
        log.info("Scheduler IS WORKING");

        List<PostponedEmail> postponedEmails = postponedEmailRepository.findAll();

        postponedEmails.forEach(this::sendPostponedEmail);
    }

    private void sendPostponedEmail(PostponedEmail postponedEmail) {
        userRepository.findByLogin(postponedEmail.getLogin())
                .ifPresent(user -> {
                    SignUpDto signUpDto = createFakeSignUpDto(user);
                    log.info("Отправка отложенного письма на адрес {}, topic: {}", signUpDto.getEmail(), postponedEmail.getTopic());

                    SimpleMailMessage message = emailService.createRegistrationMessage(signUpDto);
                    emailService.sendSimpleMessage(message);

                    // Удаляем письмо из таблицы после успешной отправки
                    postponedEmailRepository.delete(postponedEmail);
                });
    }

    private SignUpDto createFakeSignUpDto(User user) {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setEmail(user.getEmail());
        signUpDto.setLogin(user.getLogin());
        return signUpDto;
    }
}