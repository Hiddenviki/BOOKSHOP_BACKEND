package com.pet.Bookshop.dto;

import com.pet.Bookshop.enums.Covers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class BookDto {
    private Long id;

    @NotNull(message = "Заполните название книги")
    @NotBlank(message = "Заполните название книги")
    @Size(min = 1, max = 50, message = "1<=Длина названия<=50")
    @Schema(example = "Война и Мир")
    private String name; //название книги

    @NotNull(message = "Заполните название бренда")
    @NotBlank(message = "Заполните название бренда")
    @Size(min = 1, max = 50, message = "1<=Длина бренда<=50")
    @Schema(example = "Просвящение")
    private String brand; //название издания

    @Schema(example = "HARD")
    private Covers cover; //тип обложки

    @Min(value = 1, message = "Некорректное значение id")
    @Schema(example = "1")
    private Long authorId; //id автора

    @Min(value = 0, message = "Некорректное количество книг")
    @Max(value = 1000, message = "Превышение максимального количества книг 1000")
    @Schema(example = "12")
    private Integer count; //количество книг
}