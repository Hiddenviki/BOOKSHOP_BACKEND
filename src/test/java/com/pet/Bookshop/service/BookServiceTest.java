package com.pet.Bookshop.service;

import com.pet.Bookshop.dto.BookDto;
import com.pet.Bookshop.dto.filter.BookFilterDto;
import com.pet.Bookshop.entity.Author;
import com.pet.Bookshop.entity.Book;
import com.pet.Bookshop.enums.Covers;
import com.pet.Bookshop.mapper.BookMapper;
import com.pet.Bookshop.repository.BookRepository;
import com.pet.Bookshop.service.specification.BookSpecificationService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private AuthorService authorService;

    @Mock
    private BookSpecificationService bookSpecification;

    @InjectMocks
    private BookService bookService;

    @Test
    public void testGetBooks() {
        //Подготовка данных
        Book book1 = new Book();
        book1.setId(1L);
        Book book2 = new Book();
        book2.setId(2L);

        List<Book> books = Arrays.asList(book1, book2);
        when(bookRepository.findAll()).thenReturn(books);

        BookDto bookDto1 = new BookDto();
        bookDto1.setId(1L);
        BookDto bookDto2 = new BookDto();
        bookDto2.setId(2L);

        when(bookMapper.toDto(book1)).thenReturn(bookDto1);
        when(bookMapper.toDto(book2)).thenReturn(bookDto2);

        // целефая функция
        List<BookDto> result = bookService.getBooks();

        // проверки
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

    }

    @Test
    public void testGetBookById() {
        // подготовка даннных
        Long id = 1L;
        Book expectedBook = new Book();
        expectedBook.setId(id);

        //поведение
        when(bookRepository.findById(id)).thenReturn(Optional.of(expectedBook));
        when(bookMapper.toDto(expectedBook)).thenReturn(new BookDto());

        // целевая функция
        BookDto actual = bookService.getBookById(id);

        //проверки
        assertEquals(bookMapper.toDto(expectedBook), actual);
    }

    @Test
    public void testCreateBook() {
        Author author = new Author();
        author.setId(1L);

        BookDto expectedBookDto = new BookDto();
        expectedBookDto.setId(1L);
        expectedBookDto.setCover(Covers.HARD);
        expectedBookDto.setBrand("Просвящение");
        expectedBookDto.setAuthorId(1L);
        expectedBookDto.setName("Изучаем Java");

        Book expectedBook = new Book();
        expectedBook.setId(1L);
        expectedBook.setCover(Covers.HARD);
        expectedBook.setBrand("Просвящение");
        expectedBook.setName("Изучаем Java");
        expectedBook.setAuthor(author);


        when(bookMapper.toBook(expectedBookDto)).thenReturn(expectedBook);
        when(bookMapper.toDto(expectedBook)).thenReturn(expectedBookDto);
        when(authorService.getAuthor(expectedBookDto.getAuthorId())).thenReturn(author);


        BookDto actual = bookService.createBook(expectedBookDto);


        assertEquals(expectedBookDto.getId(), actual.getId());
        assertEquals(expectedBookDto.getCover(), actual.getCover());
        assertEquals(expectedBookDto.getBrand(), actual.getBrand());
        assertEquals(expectedBookDto.getName(), actual.getName());
        assertEquals(expectedBookDto.getAuthorId(), actual.getAuthorId());

    }

    @Test
    public void testEditBookById() {
        // подготовка данных
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setName("New Book Title");

        Book book = new Book();
        book.setId(1L);
        book.setName("Old Book Title");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // целевая функция
        BookDto updatedBook = bookService.editBookById(bookDto);

        // проверки
        assertEquals("New Book Title", updatedBook.getName());
        assertEquals(bookDto.getId(), updatedBook.getId());
    }

    @Test
    public void testDeleteBookById() {
        // Подготовка данных
        Long id = 1L;

        // Действие
        bookService.deleteBookById(id);

        // Проверки
        verify(bookRepository, times(1)).deleteById(id);
        // Проверяем, что метод repository.deleteById был вызван ровно 1 раз с указанным id
    }

    @Test
    public void testFilterBooks() throws NoSuchFieldException, IllegalAccessException {
        // Создаем объект фильтра
        BookFilterDto filterDto = new BookFilterDto();
        filterDto.setName("Война и Мир");

        // Создаем объект книги и устанавливаем его свойства
        Book book = new Book();
        book.setId(1L);
        book.setName("Война и Мир");

        // Создаем объект DTO книги
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setName("Война и Мир");

        // Создаем макет списка книг и добавляем в него созданную книгу
        List<Book> mockBookList = new ArrayList<>();
        mockBookList.add(book);

        //это нужно потому что иначе this.bookSpecification is null
        // Находим приватное поле "bookSpecification" в классе BookService
        Field field = BookService.class.getDeclaredField("bookSpecification");
        field.setAccessible(true); // Делаем поле доступным для изменения
        // Устанавливаем bookService поле bookSpecification в тестовый объект bookSpecification
        field.set(bookService, bookSpecification);

        // Создаем макет реальной спецификации
        BookSpecificationService bookSpecificationService = new BookSpecificationService();
        // Теперь можно настраивать поведение bookSpecification
        Specification<Book> spec = bookSpecificationService.buildSpecification(filterDto);

        // поведение макета bookSpecification при вызове метода buildSpecification с переданным фильтром
        when(bookSpecification.buildSpecification(filterDto)).thenReturn(spec);
        // поведение макета bookRepository при вызове метода findAll с переданной спецификацией
        when(bookRepository.findAll(spec)).thenReturn(mockBookList);
        // поведение макета bookMapper при вызове метода toDto с переданной книгой
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> actual = bookService.filterBooks(filterDto);

        // совпадает ли количество элементов
        assertEquals(1, actual.size());
        // совпадает ли имя книги
        assertEquals("Война и Мир", actual.get(0).getName());
    }

}
