package com.pet.Bookshop.Controllers;

import com.pet.Bookshop.DTO.AuthorDto;
import com.pet.Bookshop.Entity.Author;
import com.pet.Bookshop.Service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

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
    public AuthorDto createAuthor(@RequestBody Author author) {
        return authorService.createAuthor(author);
    }
}
