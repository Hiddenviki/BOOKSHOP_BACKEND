package com.pet.Bookshop.controller;

import com.pet.Bookshop.dto.AuthorDto;
import com.pet.Bookshop.entity.Author;
import com.pet.Bookshop.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
@Log4j2
public class AuthorController {
    private final AuthorService authorService;

    //вывод всех авторов
    @GetMapping
    public List<AuthorDto> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    //вывод конкретного автора по author_id
    @GetMapping("/{id}")
    public AuthorDto getAuthorById(@PathVariable Long id) {
        return authorService.getAuthorById(id);
    }

    //создание автора
    @PostMapping
    public AuthorDto createAuthor(@Valid @RequestBody Author author) {
        return authorService.createAuthor(author);
    }


}
