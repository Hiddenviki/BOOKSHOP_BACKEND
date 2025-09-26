package com.pet.Bookshop.service;


import com.pet.Bookshop.dto.BookDto;
import com.pet.Bookshop.dto.filter.BookFilterDto;
import com.pet.Bookshop.entity.Author;
import com.pet.Bookshop.entity.Book;
import com.pet.Bookshop.entity.User;
import com.pet.Bookshop.mapper.BookMapper;
import com.pet.Bookshop.repository.BookRepository;
import com.pet.Bookshop.security.MyUserDetails;
import com.pet.Bookshop.service.specification.BookSpecificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final AuthorService authorService;
    private final BookSpecificationService bookSpecification;
    private final UserService userService;


    public List<BookDto> getBooks() {
        log.info("BookService-getBooks: Смотрим на все книги");
        //пробегаюсь по листу и делаю из него ДТО а потом опять делаю лист
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto).toList();
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
        userService.checkAndChangeUserRole();

        log.info("BookService-createBook: создание книги {} с id автора {}", bookDto.getName(), bookDto.getAuthorId());

        Book book = bookMapper.toBook(bookDto);
        book.setAuthor(authorService.getAuthor(bookDto.getAuthorId()));
        bookRepository.save(book);

        return bookMapper.toDto(book);
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('AUTHOR') and @bookService.isAuthorOfTheBook(#bookId))")
    public void deleteBookById(Long bookId) {
        bookRepository.deleteById(bookId);
        log.info("BookService-deleteBookById: Удалили книгу с id {}", bookId);
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('AUTHOR') and @bookService.isAuthorOfTheBook(#bookDto.id))")
    public BookDto editBookById(BookDto bookDto) {
        Book book = bookRepository.findById(bookDto.getId())
                .orElseThrow(() -> new RuntimeException("Не найдена книга с id " + bookDto.getId()));

        bookMapper.update(book, bookDto);
        book = bookRepository.save(book);
        log.info("BookService-editBookById: Поменяли книгу с id {}", bookDto.getId());

        return bookMapper.toDto(book);
    }

    public List<BookDto> filterBooks(BookFilterDto filter) {
        log.info("BookService-filterBooks: фильтры: {}\n ", filter.toString());

        Specification<Book> spec = bookSpecification.buildSpecification(filter);
        List<Book> bookList = bookRepository.findAll(spec);

        return bookList.stream().map(bookMapper::toDto).toList();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }

    public boolean isAuthorOfTheBook(Long bookId) {
        User user = getCurrentUser();

        Book desiredBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга с таким id не существует " + bookId));
        Author author = desiredBook.getAuthor();

        return author.getId().equals(user.getId());
    }
}