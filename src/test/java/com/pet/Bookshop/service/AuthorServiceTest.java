package com.pet.Bookshop.service;

import com.pet.Bookshop.dto.AuthorDto;
import com.pet.Bookshop.dto.NewAuthorDto;
import com.pet.Bookshop.entity.Author;
import com.pet.Bookshop.mapper.AuthorMapper;
import com.pet.Bookshop.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
public class AuthorServiceTest {
    @Mock
    private final AuthorRepository authorRepository;
    @Mock
    private final AuthorMapper authorMapper;
    @InjectMocks
    private AuthorService authorService;  // экземпляр тестируемого сервиса авторов

    @Test
    void testGetAuthorById() {
        // подготовка тестовых данных
        Long id = 1L; //создаю id
        Author expected = new Author();  //экземпляр автора
        expected.setId(id);  // даем автору идентификатор
        expected.setFirstName("Лев");  // даем автору имя
        expected.setLastName("Толстой");  // даем автору фамилию
        expected.setBooks(Collections.emptyList()); //у него пока не будет книг

        //подготовка среды
        //когда вызывается findById, макет должен возвращать Optional, содержащий объект author
        when(authorRepository.findById(id)).thenReturn(Optional.of(expected)); //типа оно может быть пустым
        // когда вызывается toDto с объектом author,
        // должен возвращаться новый объект AuthorDto с определенными аргументами
        when(authorMapper.toDto(expected)).thenReturn(new AuthorDto(id, "Лев Толстой", Collections.emptyList()));

        // тут вызов целевого метода
        AuthorDto actual = authorService.getAuthorById(id);

        // тут проверяем результаты
        assertEquals(id, actual.getId());  // Проверяем, что идентификатор автора соответствует ожидаемому
        assertEquals("Лев Толстой", actual.getAuthorName());  // Проверяем, что имя автора соответствует ожидаемому
        assertEquals(Collections.emptyList(), actual.getBookIds());  // Проверяем, что список идентификаторов книг пуст

    }

    @Test
    void testCreateNewAuthor() {
        // подготовка тестовых данных
        //то что поступает на вход с фронта
        NewAuthorDto newAuthorDto = new NewAuthorDto(1L, "Агата", "Кристи");
        // ожидаемый объект автораДТО
        AuthorDto expectedAuthorDto = new AuthorDto();
        expectedAuthorDto.setId(1L);
        expectedAuthorDto.setAuthorName("Агата Кристи");
        // ожидаемый объект автора
        Author expectedAuthor = new Author();
        expectedAuthor.setId(1L);
        expectedAuthor.setFirstName("Агата");
        expectedAuthor.setLastName("Кристи");
        expectedAuthor.setBooks(Collections.emptyList());

        //подготовка среды
        when(authorMapper.toAuthor(newAuthorDto)).thenReturn(expectedAuthor);
        when(authorRepository.save(expectedAuthor)).thenReturn(expectedAuthor);
        when(authorMapper.toDto(expectedAuthor)).thenReturn(expectedAuthorDto);

        // тут вызов целевого метода
        AuthorDto actual = authorService.createAuthor(newAuthorDto);

        // проверка результатов
        assertEquals(newAuthorDto.getId(), actual.getId());
        assertEquals(newAuthorDto.getFirstName() + " " + newAuthorDto.getLastName(), actual.getAuthorName());
        assertEquals(Collections.emptyList(), actual.getBookIds());
    }

    @Test
    void testGetAllAuthors() {
        // подготовка данных
        //первый автор
        Author expectedAuthor1 = new Author();
        expectedAuthor1.setId(1L);
        expectedAuthor1.setFirstName("Лев");
        expectedAuthor1.setLastName("Толстой");
        expectedAuthor1.setBooks(Collections.emptyList());

        //вторая авторша
        Author expectedAuthor2 = new Author();
        expectedAuthor2.setId(2L);
        expectedAuthor2.setFirstName("Агата");
        expectedAuthor2.setLastName("Кристи");
        expectedAuthor2.setBooks(Collections.emptyList());

        List<Author> authorList = Arrays.asList(expectedAuthor1, expectedAuthor2);

        AuthorDto expectedAuthorDto1 = new AuthorDto(1L, "Лев Толстой", Collections.emptyList());
        AuthorDto expectedAuthorDto2 = new AuthorDto(2L, "Агата Кристи", Collections.emptyList());

        // подготовка поведения
        when(authorRepository.findAll()).thenReturn(authorList);
        when(authorMapper.toDto(expectedAuthor1)).thenReturn(expectedAuthorDto1);
        when(authorMapper.toDto(expectedAuthor2)).thenReturn(expectedAuthorDto2);

        // вызов целевого метода
        List<AuthorDto> actualList = authorService.getAllAuthors();

        // проверка результатов
        assertEquals(2, actualList.size());
        assertEquals(expectedAuthorDto1, actualList.get(0));
        assertEquals(expectedAuthorDto2, actualList.get(1));
    }

    @Test
    void testGetAuthor() {
        // подготовка данных
        Long id = 1L;
        Author expectedAuthor = new Author();
        expectedAuthor.setId(id);
        expectedAuthor.setFirstName("Лев");
        expectedAuthor.setLastName("Толстой");
        expectedAuthor.setBooks(Collections.emptyList());

        // подготовка среды
        when(authorRepository.findById(id)).thenReturn(Optional.of(expectedAuthor));

        // вызов целевого метода
        Author actualAuthor = authorService.getAuthor(id);

        // проверка
        assertEquals(expectedAuthor, actualAuthor);
    }

}

