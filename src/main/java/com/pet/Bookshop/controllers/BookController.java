package com.pet.Bookshop.controllers;

import com.pet.Bookshop.model.dto.BookDto;
import com.pet.Bookshop.model.filter.BookFilter;
import com.pet.Bookshop.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public List<BookDto> getBooks() {
        return bookService.getBooks();
    }

    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    public BookDto createBook(@Valid @RequestBody BookDto bookDto) {
        return bookService.createBook(bookDto);
    }

    @DeleteMapping("/{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
    }

    @PatchMapping
    public BookDto editBookById(@Valid @RequestBody BookDto bookDto) {
        return bookService.editBookById(bookDto);
    }

    @GetMapping("/filter")
    public List<BookDto> filter(@RequestBody BookFilter filter) {
        return bookService.filterBooks(filter);
    }

}