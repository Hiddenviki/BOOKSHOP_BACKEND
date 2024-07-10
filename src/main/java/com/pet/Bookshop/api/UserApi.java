package com.pet.Bookshop.api;

import com.pet.Bookshop.dto.SignInDto;
import com.pet.Bookshop.dto.SignUpDto;
import com.pet.Bookshop.dto.UserInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "User API", description = "API для работы с пользователями")
public interface UserApi {

    @Operation(
            description = "Регистрация и вход",
            summary = "Регистрация и вход"
    )
    ResponseEntity<?> signUpAndLogin(
            @RequestBody @Valid @Parameter(description = "DTO входа (логин и пароль)")
            @io.swagger.v3.oas.annotations.media.Schema(
                    example = "{\"email\": \"Victoryagraz@gmail.com\", \"login\": \"naruta2007\", \"password1\": \"Veselkova249!\", \"password2\": \"Veselkova249!\"}")
            SignUpDto signUpDto
    );

    @Operation(
            description = "Вход",
            summary = "Вход"
    )
    ResponseEntity<?> signIn(
            @RequestBody @Parameter(description = "DTO входа (логин и пароль)")
            @io.swagger.v3.oas.annotations.media.Schema(
                    example = "{\"login\": \"naruta2007\", \"password\": \"Veselkova249!\"}")
            SignInDto signInDto
    );

    @Operation(
            description = "Информация о всех пользователях",
            summary = "Информация о всех пользователях"
    )
    List<SignInDto> getUsers();

    @Operation(
            description = "Информация о текущем пользователе",
            summary = "Информация о текущем пользователе"
    )
    UserInfoDto showUserInfo();

}
