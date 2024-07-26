package com.pet.Bookshop.controller;

import com.pet.Bookshop.api.BookApi;
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
public class BookController implements BookApi {
    private final BookService bookService;

    @GetMapping
    @Override
    public List<BookDto> getBooks() {
        return bookService.getBooks();
    }

    @GetMapping("/{id}")
    @Override
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    @Override
    public BookDto createBook(@Valid @RequestBody BookDto bookDto) {
        return bookService.createBook(bookDto);
    }

    @DeleteMapping("/{id}")
    @Override
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
    }

    @PatchMapping
    @Override
    public BookDto editBookById(@Valid @RequestBody BookDto bookDto) {return bookService.editBookById(bookDto);
    }

    @GetMapping("/filter")
    @Override
    public List<BookDto> filter(@RequestBody BookFilterDto filter) {
        return bookService.filterBooks(filter);
    }

}