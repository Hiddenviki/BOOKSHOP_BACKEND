package com.pet.Bookshop.model.entity;

import com.pet.Bookshop.model.enums.Cover;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "brand", nullable = false, length = 50)
    private String brand;

    @Column(name = "cover", nullable = false, length = 5)
    @Enumerated(EnumType.STRING)
    private Cover cover;  // Enum типа обложки: твердый или мягкий переплет

    @ManyToOne(cascade = CascadeType.REFRESH) //много книг у одного автора
    private Author author;

    @Column(name = "count", nullable = false, length = 50)
    private Integer count;

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", cover=" + cover +
                ", author=" + author.toString() +
                ", count=" + count +
                '}';
    }
}
