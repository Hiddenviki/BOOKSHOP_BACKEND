package com.pet.Bookshop.Service;


import com.pet.Bookshop.DTO.BookDto;
import com.pet.Bookshop.Entity.Book;
import com.pet.Bookshop.Mapper.BookMapper;
import com.pet.Bookshop.Repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final AuthorService authorService;
    //private final Validator validator;


    public List<BookDto> getBooks() {
        log.info("BookService-getBooks: Смотрим на все книги");
        //беру всё из репозитория книг
        List<Book> books = bookRepository.findAll();
        //пробегаюсь по листу и делаю из него ДТО а потом опять делаю лист
        return books.stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    public BookDto getBookById(Long id) {
        log.info("BookService-getBookById: Показана книга с id {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Не нашли книгу с таким айди: " + id));
        //перед возвратом делаю ДТО
        return bookMapper.toDto(book);
    }


    @Transactional
    public BookDto createBook(BookDto bookDto) {
        log.info("-----------------\nBookService-createBook: в ДТО {} id автора {}", bookDto.getName(), bookDto.getAuthorId());

        Book book = bookMapper.toBook(bookDto); //сначала книгу из ДТО

        // Проводим проверку перед сохранением книги в базу
//        Set<ConstraintViolation<Book>> violations = validator.validate(book);
//        if (!violations.isEmpty()) {
//            throw new RuntimeException("Невозможно создать книгу из-за ошибок валидации: " + violations);
//        }

        book.setAuthor(authorService.getAuthor(bookDto.getAuthorId())); //устанавливаю автора
        bookRepository.save(book); //сохраняю

        log.info("BookService-createBook: Создали новую книгу");

        return bookMapper.toDto(book); //возвращаю ДТО
    }

    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
        log.info("BookService-deleteBookById: Удалили книгу с id {}", id);
    }


    @Transactional
    public BookDto editBookById(BookDto bookDto) {
        log.info("BookService-editBookByI: редактирование книги с id {}", bookDto.getId());
        //ищу такую книгу вдруг такой нет
        Book book = bookRepository.findById(bookDto.getId())
                .orElseThrow(() -> new RuntimeException("Не нашли книгу с таким айди(или забыли поменять аргумент в запросе): " + bookDto.getId()));

        bookMapper.update(book, bookDto);//кладём новые значения

        //надо сохранить перед возвращением
        book = bookRepository.save(book);
        log.info("BookService-editBookById: Поменяли книгу с id {}", bookDto.getId());
        //возвращаю в виде ДТО
        return bookMapper.toDto(book);
    }

}
