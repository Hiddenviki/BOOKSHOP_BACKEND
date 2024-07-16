package com.pet.Bookshop.service;


import com.pet.Bookshop.dto.BookDto;
import com.pet.Bookshop.dto.filter.BookFilterDto;
import com.pet.Bookshop.entity.Book;
import com.pet.Bookshop.mapper.BookMapper;
import com.pet.Bookshop.repository.BookRepository;
import com.pet.Bookshop.service.specification.BookSpecificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final AuthorService authorService;
    private BookSpecificationService bookSpecification;


    public List<BookDto> getBooks() {
        log.info("BookService-getBooks: Смотрим на все книги");
        //пробегаюсь по листу и делаю из него ДТО а потом опять делаю лист
        return bookRepository.findAll().stream()
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
        log.info("BookService-createBook: в ДТО {} id автора {}", bookDto.getName(), bookDto.getAuthorId());

        Book book = bookMapper.toBook(bookDto); //сначала книгу из ДТО

        book.setAuthor(authorService.getAuthor(bookDto.getAuthorId())); //устанавливаю автора

        log.info("BookService-createBook: создание новой книги");
        bookRepository.save(book); //сохраняю

        return bookMapper.toDto(book); //возвращаю ДТО
    }

    @Transactional
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

    public List<BookDto> filterBooks(BookFilterDto filter) {
        log.info("BookService-filterBooks: фильтры: {}\n ", filter.toString());

        Specification<Book> spec = bookSpecification.buildSpecification(filter);
        List<Book> bookList = bookRepository.findAll(spec);

        return bookList.stream().map(bookMapper::toDto).collect(Collectors.toList());
    }
}