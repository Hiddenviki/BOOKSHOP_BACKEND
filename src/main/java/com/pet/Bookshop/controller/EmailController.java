package com.pet.Bookshop.controller;

import com.pet.Bookshop.api.EmailApi;
import com.pet.Bookshop.dto.EmailDto;
import com.pet.Bookshop.service.EmailService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/emails")
@RequiredArgsConstructor
public class EmailController implements EmailApi {

    private final EmailService emailService;

/**
 * отправка сообщения от имени администратора
 * @param emailDto
 * @return String
 * * */
    @PostMapping("/send")
    @Override
    public String sendAdminEmail(@RequestBody @Valid EmailDto emailDto) {
        return emailService.sendAdminEmail(emailDto);
    }


}
