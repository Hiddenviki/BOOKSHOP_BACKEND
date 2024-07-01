package com.pet.Bookshop.controllers;

import com.pet.Bookshop.configuration.security.userdetails.MyUserDetailService;
import com.pet.Bookshop.model.dto.SignInDto;
import com.pet.Bookshop.model.dto.SignUpDto;
import com.pet.Bookshop.model.dto.TokenDto;
import com.pet.Bookshop.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final MyUserDetailService myUserDetailService;

    //регистрация jwt
    @PostMapping("/signUp")
    public ResponseEntity<?> signUpAndLogin(@RequestBody @Valid SignUpDto signUpDto) {
        TokenDto tokenResponse = userService.registerUserAndGetToken(signUpDto); // Регистрация пользователя
        return ResponseEntity.ok(tokenResponse);
    }

    //вход jwt
    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody SignInDto signInDto) {
        return ResponseEntity.ok(userService.login(signInDto));
    }

    //список пользователей
    @GetMapping
    public List<SignInDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/showUserInfo")
    @ResponseBody
    public String showUserInfo() {
        //загружаем юзера
        UserDetails userDetails = myUserDetailService.getCurrentUser();
        return userDetails.toString();
    }
}
