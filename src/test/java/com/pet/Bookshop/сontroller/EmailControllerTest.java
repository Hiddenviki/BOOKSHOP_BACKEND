package com.pet.Bookshop.сontroller;

import com.pet.Bookshop.controller.EmailController;
import com.pet.Bookshop.dto.EmailDto;
import com.pet.Bookshop.service.EmailService;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmailControllerTest {
    @Mock
    private EmailService emailService;
    @InjectMocks
    private EmailController emailController;

    @Test
    public void testSendAdminEmail_WhenValidEmailDtoProvided_ExpectSuccess() {

        EmailDto emailDto = new EmailDto("test@example.com", "Test Subject", "Test email content");

        when(emailService.sendAdminEmail(emailDto)).thenReturn("Email sent successfully");


        String result = emailController.sendAdminEmail(emailDto);


        assertEquals("Email sent successfully", result);
    }

    @Test
    public void testSendAdminEmail_WhenInvalidEmailDtoProvided_ExpectBadRequestException() {
        EmailDto emailDto = new EmailDto("email", "Test Subject", "Test email content");
        when(emailService.sendAdminEmail(emailDto)).thenThrow(new MailSendException("Неправильный адрес email"));

        assertThrows(MailSendException.class, () -> emailController.sendAdminEmail(emailDto));
    }
}