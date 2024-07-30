package com.pet.Bookshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewAuthorDto {
    @Schema(example = "1")
    private Long id; //id автора

    @Schema(example = "Лев")
    private String firstName;

    @Schema(example = "Толстой")
    private String lastName;
}
