package com.pet.Bookshop.mapper;

import com.pet.Bookshop.model.dto.BookDto;
import com.pet.Bookshop.model.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper
public interface BookMapper {

// автор назначается в сервисе

    Book toBook(BookDto bookDto);

    @Mapping(target = "authorId", expression = "java(book.getAuthor().getId())")
    BookDto toDto(Book book);

    void update(@MappingTarget Book book, BookDto bookDto);

}