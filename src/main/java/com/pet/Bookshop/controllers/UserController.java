package com.pet.Bookshop.controllers;

import com.pet.Bookshop.model.dto.SignInDto;
import com.pet.Bookshop.model.dto.SignUpDto;
import com.pet.Bookshop.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //регистрация новых пользователей
    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpDto signUpDto) {
        SignInDto signInDto = userService.createUser(signUpDto);
        return ResponseEntity.ok(signInDto);
    }

    //вход
    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody SignInDto signInDto) {
        return ResponseEntity.ok(userService.login(signInDto));
    }

    //список пользователей
    @GetMapping
    public List<SignInDto> getBooks() {
        return userService.getUsers();
    }

}
