package com.pet.Bookshop.DTO;

import com.pet.Bookshop.Enum.Cover;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    @Id
    private int id;
    private String  name;
    private String brand;
    private Cover cover;
    private String author;
    private Integer count;
}