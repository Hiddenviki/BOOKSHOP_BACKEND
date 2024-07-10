package com.pet.Bookshop.dto.filter;

import com.pet.Bookshop.enums.Covers;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BookFilterDto {

    @Schema(example = "Война и Мир")
    private String name;
    @Schema(example = "Просвящение")
    private String brand;
    @Schema(example = "HARD")
    private Covers cover;
    @Schema(example = "Лев")
    private String authorFirstName;
    @Schema(example = "Толстой")
    private String authorLastName;
    @Schema(example = "12")
    private Integer count;
}