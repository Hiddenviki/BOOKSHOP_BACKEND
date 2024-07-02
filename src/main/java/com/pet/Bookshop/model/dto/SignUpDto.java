package com.pet.Bookshop.model.dto;

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
    private String email;

    @NotNull(message = "Заполните login")
    @NotBlank(message = "Заполните login")
    @Size(min = 1, max = 50, message = "1<=Длина login<=50")
    private String login;

    @NotNull(message = "Заполните password")
    @NotBlank(message = "Заполните password")
    @Size(min = 8, max = 50, message = "8<=Длина пароля<=50")
    private String password1;

    @NotNull(message = "Повторите password")
    @NotBlank(message = "Повторите password")
    private String password2;

    private String text;

    @AssertTrue(message = "Пароли не совпадают")
    public boolean isPasswordsMatch() {
        return password1 != null && password1.equals(password2);
    }
}
