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
    private Long id;
    private String  name; //название книги
    private String brand; //название издания
    private Cover cover; //тип обложки
    private Long authorId; //id автора
    private Integer count; //количество книг

}