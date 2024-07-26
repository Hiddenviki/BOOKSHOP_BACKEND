package com.pet.Bookshop.service;

import com.pet.Bookshop.dto.SignUpDto;
import com.pet.Bookshop.entity.PostponedEmail;
import com.pet.Bookshop.entity.User;
import com.pet.Bookshop.repository.PostponedEmailRepository;
import com.pet.Bookshop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.SimpleMailMessage;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class SchedulerServiceTest {

    @Mock
    private PostponedEmailRepository postponedEmailRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private SchedulerService schedulerService;

    @Test
    void testCheckPostponedEmails() {
        // Определите тестовые данные
        PostponedEmail postponedEmail = new PostponedEmail();
        postponedEmail.setLogin("testUser");
        postponedEmail.setTopic("Test Topic");

        User user = new User();
        user.setLogin("testUser");
        user.setEmail("test@example.com");

        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setEmail(user.getEmail());
        signUpDto.setLogin(user.getLogin());

        SimpleMailMessage testMessage = new SimpleMailMessage();
        testMessage.setTo(signUpDto.getEmail());
        testMessage.setSubject("Registration Subject");
        testMessage.setText("Registration Message");

        when(postponedEmailRepository.findAll()).thenReturn(Collections.singletonList(postponedEmail));
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(user));
        when(emailService.createRegistrationMessage(any(SignUpDto.class))).thenReturn(testMessage);

        // Вызов метода для тестирования
        schedulerService.checkPostponedEmails();

        // Проверки
        verify(postponedEmailRepository, times(1)).findAll();
        verify(userRepository, times(1)).findByLogin("testUser");
        verify(emailService, times(1)).createRegistrationMessage(argThat(dto -> dto.getEmail().equals("test@example.com") && dto.getLogin().equals("testUser")));
        verify(emailService, times(1)).sendSimpleMessage(testMessage);
        verify(postponedEmailRepository, times(1)).delete(postponedEmail);
    }
}
