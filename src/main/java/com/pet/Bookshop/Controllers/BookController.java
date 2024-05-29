package com.pet.Bookshop.Controllers;

import com.pet.Bookshop.DTO.BookDto;
import com.pet.Bookshop.Entity.Book;
import com.pet.Bookshop.Service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;


    @GetMapping
    public List<BookDto> getBooks() {
        return bookService.getBooks();
    }

    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    public BookDto createBook(@RequestBody BookDto bookDto){
        return bookService.createBook(bookDto);
    }

    //почему не доступно @DeleteMapping??
//    @PostMapping
//    public void deleteBookById(@RequestBody Long id) {
//        bookService.deleteBookById(id);
//    }
    @DeleteMapping("/{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
    }


    //добавить изменение информации о книге /books/{id}/edit потом
    //@PatchMapping("/{id}")
    //public Book editBookById(@PathVariable Long id){

    //    return ;
    //}



}