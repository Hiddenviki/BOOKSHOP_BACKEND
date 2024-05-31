package com.pet.Bookshop.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id=2L;

    @Column(name = "first_name")
    private String firstName="defaultFirstName";

    @Column(name = "last_name")
    private String lastName="defaultLastName";

    //у одного автора много книг
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true) //если книга не ссылается ни на одного автора то ее надо удалить
    private List<Book> books = new ArrayList<>();


}