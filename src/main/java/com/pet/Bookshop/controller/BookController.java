package com.pet.Bookshop.controller;

import com.pet.Bookshop.dto.BookDto;
import com.pet.Bookshop.dto.filter.BookFilterDto;
import com.pet.Bookshop.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<BookDto> filter(@RequestBody BookFilterDto filter) {
        return bookService.filterBooks(filter);
    }

}