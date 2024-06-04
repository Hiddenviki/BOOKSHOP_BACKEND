package com.pet.Bookshop.Mapper;

import com.pet.Bookshop.Entity.Book;
import com.pet.Bookshop.DTO.BookDto;
import org.mapstruct.*;


@Mapper
public interface BookMapper {

// автор назначается в сервисе

    Book toBook(BookDto bookDto);

    @Mapping(target = "authorId", expression = "java(book.getAuthor().getId())")
    BookDto toDto(Book book);

    void update(@MappingTarget Book book, BookDto bookDto);

}