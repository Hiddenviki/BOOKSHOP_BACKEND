package com.pet.Bookshop.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//регистрация
@Data
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class SignUpDto {

    @NotNull(message = "Заполните email")
    @NotBlank(message = "Заполните email")
    @Size(min = 1, max = 50, message = "1<=Длина email<=50")
    @Email
    @Schema(example = "victoryagraz@gmail.com")
    private String email;

    @NotNull(message = "Заполните login")
    @NotBlank(message = "Заполните login")
    @Size(min = 1, max = 50, message = "1<=Длина login<=50")
    @Schema(example = "naruta2007")
    private String login;

    @NotNull(message = "Заполните password")
    @NotBlank(message = "Заполните password")
    @Size(min = 8, max = 50, message = "8<=Длина пароля<=50")
    @Schema(example = "password!")
    private String password1;

    @NotNull(message = "Повторите password")
    @NotBlank(message = "Повторите password")
    @Schema(example = "password!")
    private String password2;

    @AssertTrue(message = "Пароли не совпадают")
    public boolean isPasswordsMatch() {
        return password1 != null && password1.equals(password2);
    }
}