package com.pet.Bookshop.сontroller;

import com.pet.Bookshop.controller.AuthorController;
import com.pet.Bookshop.dto.AuthorDto;
import com.pet.Bookshop.dto.NewAuthorDto;
import com.pet.Bookshop.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class AuthorControllerTest {
    @Mock
    private AuthorService authorService;
    @InjectMocks
    private AuthorController authorController;


    @Test
    void testGetAllAuthors() {
        // данные
        List<AuthorDto> expectedAuthors = new ArrayList<>(); // Prepare expected data
        when(authorService.getAllAuthors()).thenReturn(expectedAuthors);

        // метод
        List<AuthorDto> actualAuthors = authorController.getAllAuthors();

        //проверка
        assertEquals(expectedAuthors, actualAuthors);
    }

    @Test
    void testGetAuthorById() {
        // данные
        Long authorId = 1L;
        AuthorDto expectedAuthor = new AuthorDto();

        //поведение
        when(authorService.getAuthorById(authorId)).thenReturn(expectedAuthor);

        //метод
        AuthorDto actualAuthor = authorController.getAuthorById(authorId);

        //проверка
        assertEquals(expectedAuthor, actualAuthor);
    }

    @Test
    void testCreateAuthor() {
        // данный
        NewAuthorDto newAuthorDto = new NewAuthorDto(1L, "Лев", "Толстой");
        AuthorDto expectedAuthor = new AuthorDto();

        //поведение
        when(authorService.createAuthor(newAuthorDto)).thenReturn(expectedAuthor);

        // метод
        AuthorDto actualAuthor = authorController.createAuthor(newAuthorDto);

        // проверка
        assertEquals(expectedAuthor, actualAuthor);
    }
}