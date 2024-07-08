package com.pet.Bookshop.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "authors")
@Valid
public class Author {
    @Id
    //не генерируется потому что туда передается id юзера
    //@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Заполните имя автора")
    @NotBlank(message = "Заполните имя автора")
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @NotNull(message = "Заполните Фамилию автора")
    @NotBlank(message = "Заполните Фамилию автора")
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    //у одного автора много книг
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    //если книга не ссылается ни на одного автора то ее надо удалить
    private List<Book> books = new ArrayList<>();


    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}