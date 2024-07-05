package com.pet.Bookshop.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class EmailDto {

    @NotNull(message = "Заполните email получателя")
    @NotBlank(message = "Заполните email получателя")
    @Size(min = 1, max = 50, message = "1<=Длина email<=50")
    @Email
    String to;

    @NotNull(message = "Заполните тему письма")
    @NotBlank(message = "Заполните тему письма")
    @Size(min = 1, max = 50, message = "Покороче")
    String subject;

    @NotNull(message = "Заполните письмо")
    @NotBlank(message = "Заполните письмо")
    String text;

}
