package com.pet.Bookshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto {
    @Schema(example = "1")
    private Long id; //id автора

    @Schema(example = "Лев Толстой")
    private String authorName; //first_name+last_name | имя+фамилия

    @Schema(example = "[1]")
    private List<Long> bookIds = new ArrayList<>(); //список книг автора
}
