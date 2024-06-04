package com.pet.Bookshop.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto {

    private Long id; //id автора

    private String authorName; //first_name+last_name | имя+фамилия

    private List<Long> bookIds = new ArrayList<>(); //список книг автора
}
