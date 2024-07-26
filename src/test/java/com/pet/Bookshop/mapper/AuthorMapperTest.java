package com.pet.Bookshop.mapper;

import com.pet.Bookshop.dto.AuthorDto;
import com.pet.Bookshop.dto.NewAuthorDto;
import com.pet.Bookshop.entity.Author;
import com.pet.Bookshop.entity.Book;
import com.pet.Bookshop.enums.Covers;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthorMapperTest {
    //вызываем AuthorMapper через getMapper
    private final AuthorMapper authorMapper = Mappers.getMapper(AuthorMapper.class);

    @Test
    void testToAuthorMapping() {
        //Данные
        NewAuthorDto newAuthorDto = new NewAuthorDto(1L, "Лев", "Толстой");

        Author expectedAuthor = new Author();
        expectedAuthor.setId(1L);
        expectedAuthor.setFirstName("Лев");
        expectedAuthor.setLastName("Толстой");
        expectedAuthor.setBooks(Collections.emptyList());

        //вызов метода
        Author actualAuthor = authorMapper.toAuthor(newAuthorDto);

        //проверки
        assertEquals(expectedAuthor.getId(), actualAuthor.getId());
        assertEquals(expectedAuthor.getFirstName(), actualAuthor.getFirstName());
        assertEquals(expectedAuthor.getLastName(), actualAuthor.getLastName());
        assertEquals(expectedAuthor.getBooks(), actualAuthor.getBooks());
    }

    @Test
    void testToDtoMapping() {
        // данные
        Author author = new Author();
        author.setId(1L);
        author.setFirstName("Лев");
        author.setLastName("Толстой");

        Book book1 = new Book();
        book1.setId(1L);
        Book book2 = new Book();
        book2.setId(2L);
        author.setBooks(Arrays.asList(book1, book2));

        AuthorDto expectedAuthorDto = new AuthorDto(1L, "Лев Толстой", Arrays.asList(1L, 2L));

        //вызов метода
        AuthorDto actualAuthorDto = authorMapper.toDto(author);

        //проверки
        assertEquals(expectedAuthorDto.getId(), actualAuthorDto.getId());
        assertEquals(expectedAuthorDto.getAuthorName(), actualAuthorDto.getAuthorName());
        assertEquals(expectedAuthorDto.getBookIds(), actualAuthorDto.getBookIds());

    }

    @Test
    void testExtractBookIds() {
        // данные
        List<Book> books = Arrays.asList(new Book(1L, "Book1", "Brand1", Covers.HARD, null, 10),
                new Book(2L, "Book2", "Brand2", Covers.SOFT, null, 15));

        // вызов метода
        List<Long> bookIds = authorMapper.extractBookIds(books);

        //проверка
        assertEquals(Arrays.asList(1L, 2L), bookIds);
    }
}
