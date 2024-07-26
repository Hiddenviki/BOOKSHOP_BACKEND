package com.pet.Bookshop.сontroller;

import com.pet.Bookshop.controller.BookController;
import com.pet.Bookshop.dto.BookDto;
import com.pet.Bookshop.dto.filter.BookFilterDto;
import com.pet.Bookshop.enums.Covers;
import com.pet.Bookshop.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @Test
    void testGetBooks() {
        List<BookDto> expectedBooks = new ArrayList<>();
        when(bookService.getBooks()).thenReturn(expectedBooks);

        List<BookDto> actualBooks = bookController.getBooks();

        assertEquals(expectedBooks, actualBooks);
    }

    @Test
    void testGetBookById() {
        Long bookId = 1L;
        BookDto expectedBook = new BookDto();
        expectedBook.setId(bookId);

        when(bookService.getBookById(bookId)).thenReturn(expectedBook);

        BookDto actualBook = bookController.getBookById(bookId);

        assertEquals(expectedBook, actualBook);
    }

    @Test
    void testCreateBook() {

        BookDto newBookDto = new BookDto(1L, "Война и Мир", "Просвящение", Covers.HARD, 1L, 12);
        BookDto expectedBook = new BookDto(1L, "Война и Мир", "Просвящение", Covers.HARD, 1L, 12);

        when(bookService.createBook(newBookDto)).thenReturn(expectedBook);

        BookDto actualBook = bookController.createBook(newBookDto);

        assertEquals(expectedBook, actualBook);
    }

    @Test
    void testDeleteBookById() {
        Long bookId = 1L;

        assertDoesNotThrow(() -> bookController.deleteBookById(bookId));

        verify(bookService).deleteBookById(bookId);
    }

    @Test
    void testEditBookById() {
        BookDto editedBookDto = new BookDto(1L, "Война и Мир", "Просвящение", Covers.HARD, 1L, 12);
        BookDto expectedEditedBook = new BookDto();

        when(bookService.editBookById(editedBookDto)).thenReturn(expectedEditedBook);

        BookDto actualEditedBook = bookController.editBookById(editedBookDto);

        assertEquals(expectedEditedBook, actualEditedBook);
    }

    @Test
    void testFilter() {
        // Данные
        BookFilterDto filterDto = new BookFilterDto();
        filterDto.setAuthorFirstName("Лев");

        List<BookDto> expectedFilteredBooks = new ArrayList<>();
        expectedFilteredBooks.add(new BookDto(1L, "Война и мир","Просвещение", Covers.HARD, 1L, 12));

        when(bookService.filterBooks(filterDto)).thenReturn(expectedFilteredBooks);

        List<BookDto> actualFilteredBooks = bookController.filter(filterDto);

        assertEquals(expectedFilteredBooks, actualFilteredBooks);
    }
}