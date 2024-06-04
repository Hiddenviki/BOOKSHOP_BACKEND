package com.pet.Bookshop.Controllers;

import com.pet.Bookshop.DTO.AuthorDto;
import com.pet.Bookshop.Entity.Author;
import com.pet.Bookshop.Service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //отправляем сообщение об ошибках на фронт
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
