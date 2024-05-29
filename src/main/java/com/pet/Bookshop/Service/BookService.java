package com.pet.Bookshop.Service;

import com.pet.Bookshop.DTO.BookDto;
import com.pet.Bookshop.Entity.Book;
import com.pet.Bookshop.Repository.BookRepository;
import com.pet.Bookshop.Mapper.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public List<BookDto> getBooks() {
        //беру всё из репозитория книг
        List<Book> books = bookRepository.findAll();
        //пробегаюсь по листу и делаю из него ДТО а потом опять делаю лист
        return books.stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
}

    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Не нашли книгу с таким айди: " + id));
        //перед возвратом делаю ДТО
        return bookMapper.toDto(book);
    }

    public BookDto createBook(BookDto bookDto) {
        Book book = bookMapper.toBook(bookDto); //сначала книгу из ДТО
        book = bookRepository.save(book); //сохраняю
        return bookMapper.toDto(book); //но возвращаю все-равно ДТО
    }

    //тут можно без DTO?
    public void deleteBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Не нашли книгу с таким айди: " + id));
        bookRepository.delete(book);
    }
}
