package com.pet.Bookshop.mapper;

import com.pet.Bookshop.dto.BookDto;
import com.pet.Bookshop.entity.Author;
import com.pet.Bookshop.entity.Book;
import com.pet.Bookshop.enums.Covers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class BookMapperTest {

    private final BookMapper bookMapper = Mappers.getMapper(BookMapper.class);;

    @Test
    void testToBook() {
        // тестовые данные
        BookDto bookDto = new BookDto
                (
                        1L,
                        "Война и Мир",
                        "Просвящение",
                        Covers.HARD,
                        1L,
                        12);

        // метод
        Book book = bookMapper.toBook(bookDto);

        // Проверяем результат
        assertEquals(bookDto.getId(), book.getId());
        assertEquals(bookDto.getName(), book.getName());
        assertEquals(bookDto.getBrand(), book.getBrand());
        assertEquals(bookDto.getCover(), book.getCover());
        assertNull(book.getAuthor()); // Для этого теста свойство author не должно быть заполнено
        assertEquals(bookDto.getCount(), book.getCount());
    }

    @Test
    void testToDto() {
        // тестовые данные
        Author author = new Author(); // Предположим, что у вас есть класс Author
        author.setId(1L);

        Book book = new Book(1L, "Война и Мир", "Просвящение", Covers.HARD, author, 12);

        //метод
        BookDto bookDto = bookMapper.toDto(book);

        // Проверяем результат
        assertEquals(book.getId(), bookDto.getId());
        assertEquals(book.getName(), bookDto.getName());
        assertEquals(book.getBrand(), bookDto.getBrand());
        assertEquals(book.getCover(), bookDto.getCover());
        assertEquals(book.getAuthor().getId(), bookDto.getAuthorId());
        assertEquals(book.getCount(), bookDto.getCount());
    }

    @Test
    void testUpdate() {
        Author author = new Author();
        author.setId(2L); // Устанавливаем id автора

        Book existingBook = new Book(1L, "Война и Мир", "Просвящение", Covers.HARD, author, 12);

        //новая измененная книга другой брен и другое количество
        BookDto newBookDto = new BookDto(1L, "Война и Мир", "другой бренд", Covers.SOFT, 2L, 10);

        // Выполняем метод
        bookMapper.update(existingBook, newBookDto);

        // Проверяем, что книга была изменена
        assertEquals(newBookDto.getId(), existingBook.getId());
        assertEquals(newBookDto.getName(), existingBook.getName());
        assertEquals(newBookDto.getBrand(), existingBook.getBrand());
        assertEquals(newBookDto.getCover(), existingBook.getCover());
        assertEquals(newBookDto.getCount(), existingBook.getCount());
        assertEquals(newBookDto.getAuthorId(), existingBook.getAuthor().getId());
    }
}