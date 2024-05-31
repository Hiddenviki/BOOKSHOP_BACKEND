package com.pet.Bookshop.Service;

import com.pet.Bookshop.DTO.AuthorDto;
import com.pet.Bookshop.DTO.BookDto;
import com.pet.Bookshop.Entity.Author;
import com.pet.Bookshop.Entity.Book;
import com.pet.Bookshop.Mapper.AuthorMapper;
import com.pet.Bookshop.Repository.AuthorRepository;
import com.pet.Bookshop.Repository.BookRepository;
import com.pet.Bookshop.Mapper.BookMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final AuthorMapper authorMapper;
    private final AuthorRepository authorRepository;

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
        log.info("BookService-getBookById: Показана книга с id "+id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Не нашли книгу с таким айди: " + id));
        //перед возвратом делаю ДТО
        return bookMapper.toDto(book);
    }

    //тут как-то надо проверить если id автора без значения (нет автора)
    public BookDto createBook(BookDto bookDto) {
        Book book = bookMapper.toBook(bookDto); //сначала книгу из ДТО
        book = bookRepository.save(book); //сохраняю
        log.info("BookService-createBook: Создали новую книгу");
        return bookMapper.toDto(book); //но возвращаю все-равно ДТО
    }

    //тут можно без DTO?
    public void deleteBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Не нашли книгу с таким айди: " + id));
        log.info("BookService-deleteBookById: Удалили книгу с id "+id);
        bookRepository.delete(book);
    }


    //проверить правильно ли кладется автор что там с автором вообще
    public BookDto editBookById(BookDto bookDto) {
        //ищу такую книгу вдруг такой нет
        Book book = bookRepository.findById(bookDto.getId())
                .orElseThrow(() -> new RuntimeException("Не нашли книгу с таким айди: " + bookDto.getId()));

        //кладём новые значения
        book.setName(bookDto.getName());
        book.setBrand(bookDto.getBrand());
        book.setCover(bookDto.getCover());
        Long authorId = bookDto.getAuthorId(); //берем id автора
        //надо найти автора по id
        Author author = (authorRepository.findById(authorId))
                .orElseThrow(()->new RuntimeException("не нашли автора по id "+authorId)); //тут должен быть автор хз дто или нет пока
        //и положить его сюда
        book.setAuthor(author);
        book.setCount(bookDto.getCount());
        //надо сохранить перед возвращением
        book = bookRepository.save(book);
        log.info("BookService-editBookById: Поменяли книгу с id "+bookDto.getId());
        //возвращаю в виде ДТО
        return bookMapper.toDto(book);
    }

}
