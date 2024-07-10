package com.pet.Bookshop.dto.filter;

import com.pet.Bookshop.enums.Covers;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(example = "{\"name\": \"Война и Мир\"}")
public class BookFilterDto {

    private String name;
    private String brand;
    private Covers cover;
    private String authorFirstName;
    private String authorLastName;
    private Integer count;
}