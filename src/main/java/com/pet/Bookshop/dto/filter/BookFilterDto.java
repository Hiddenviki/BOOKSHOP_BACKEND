package com.pet.Bookshop.dto.filter;

import com.pet.Bookshop.enums.Covers;
import lombok.Data;

@Data
public class BookFilterDto {

    private String name;
    private String brand;
    private Covers cover;
    private String authorFirstName;
    private String authorLastName;
    private Integer count;
}