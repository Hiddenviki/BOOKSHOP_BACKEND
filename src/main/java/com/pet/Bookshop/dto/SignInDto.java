package com.pet.Bookshop.dto;

import com.pet.Bookshop.enums.Roles;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "1")
    private Long id;
    @Schema(example = "victoryagraz@gmail.com")
    private String email;
    @Schema(example = "GUEST")
    private Roles role;

    @NotNull(message = "Заполните login")
    @NotBlank(message = "Заполните login")
    @Size(min = 1, max = 50, message = "1<=Длина login<=50")
    @Schema(example = "naruta2007")
    private String login;

    @NotNull(message = "Заполните password")
    @NotBlank(message = "Заполните password")
    @Size(min = 8, max = 50, message = "8<=Длина пароля<=50")
    @Schema(example = "password!")
    private String password;
}
