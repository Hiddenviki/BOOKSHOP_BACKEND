package com.pet.Bookshop.controller;

import com.pet.Bookshop.api.UserApi;
import com.pet.Bookshop.dto.SignInDto;
import com.pet.Bookshop.dto.SignUpDto;
import com.pet.Bookshop.dto.TokenDto;
import com.pet.Bookshop.dto.UserInfoDto;
import com.pet.Bookshop.service.MyUserDetailService;
import com.pet.Bookshop.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserService userService;
    private final MyUserDetailService myUserDetailService;

    //регистрация jwt
    @PostMapping("/signUp")
    @Override
    public ResponseEntity<?> signUpAndLogin(@RequestBody @Valid SignUpDto signUpDto) {
        TokenDto tokenResponse = userService.registerUser(signUpDto); // Регистрация пользователя
        return ResponseEntity.ok(tokenResponse);
    }

    //вход jwt
    @PostMapping("/signIn")
    @Override
    public ResponseEntity<?> signIn(@RequestBody SignInDto signInDto) {
        return ResponseEntity.ok(userService.login(signInDto));
    }

    //список пользователей
    @GetMapping
    @Override
    public List<SignInDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/showUserInfo")
    @ResponseBody
    @Override
    public UserInfoDto showUserInfo() {
        return myUserDetailService.showUserInfo();
    }
}
