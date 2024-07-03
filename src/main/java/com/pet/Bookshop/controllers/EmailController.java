package com.pet.Bookshop.controllers;

import com.pet.Bookshop.model.dto.EmailDto;
import com.pet.Bookshop.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/emails")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    //отправка сообщения от имени администратора
    @PostMapping("/send")
    public String sendAdminEmail(@RequestBody @Valid EmailDto emailDto) {
        return emailService.sendAdminEmail(emailDto);
    }


}
