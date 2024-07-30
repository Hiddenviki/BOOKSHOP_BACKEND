package com.pet.Bookshop.api;

import com.pet.Bookshop.dto.SignInDto;
import com.pet.Bookshop.dto.SignUpDto;
import com.pet.Bookshop.dto.UserInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "User API", description = "API для работы с пользователями")
public interface UserApi {
    @Operation(
            description = "Регистрация и вход",
            summary = "Регистрация и вход"
    )
    ResponseEntity<?> signUpAndLogin(
            @Parameter(description = "DTO регистрации")
            SignUpDto signUpDto
    );

    @Operation(
            description = "Вход",
            summary = "Вход"
    )
    ResponseEntity<?> signIn(
            @Parameter(description = "DTO входа (логин и пароль)")
            SignInDto signInDto
    );

    @Operation(
            description = "Информация о всех пользователях",
            summary = "Информация о всех пользователях"
    )
    List<SignInDto> getUsers();

    @Operation(
            description = "Подтверждение регистрации пользователя",
            summary = "Подтверждение регистрации"
    )
    UserInfoDto verifyRegistration(
            @Parameter(description = "UUID пользователя")
            String uuid);

    @Operation(
            description = "Информация о текущем пользователе",
            summary = "Информация о текущем пользователе"
    )
    UserInfoDto showUserInfo();
}
