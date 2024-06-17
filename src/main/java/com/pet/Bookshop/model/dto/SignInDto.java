package com.pet.Bookshop.model.dto;

import com.pet.Bookshop.model.enums.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//вход
@Data
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class SignInDto {

    private Long id;
    private String email;
    private Role role;

    @NotNull(message = "Заполните login")
    @NotBlank(message = "Заполните login")
    @Size(min = 1, max = 50, message = "1<=Длина login<=50")
    private String login;

    @NotNull(message = "Заполните password")
    @NotBlank(message = "Заполните password")
    @Size(min = 8, max = 50, message = "8<=Длина пароля<=50")
    private String password;

}
