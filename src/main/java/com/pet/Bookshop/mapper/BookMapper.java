package com.pet.Bookshop.mapper;

import com.pet.Bookshop.model.entity.Book;
import com.pet.Bookshop.model.dto.BookDto;
import org.mapstruct.*;


@Mapper
public interface BookMapper {

// автор назначается в сервисе

    Book toBook(BookDto bookDto);

    @Mapping(target = "authorId", expression = "java(book.getAuthor().getId())")
    BookDto toDto(Book book);

    void update(@MappingTarget Book book, BookDto bookDto);

}