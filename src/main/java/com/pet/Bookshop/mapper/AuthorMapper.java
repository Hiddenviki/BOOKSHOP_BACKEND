package com.pet.Bookshop.mapper;

import com.pet.Bookshop.dto.AuthorDto;
import com.pet.Bookshop.entity.Author;
import com.pet.Bookshop.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;


@Mapper
public abstract class AuthorMapper {

    @Mapping(target = "books", ignore = true)
    public abstract Author toAuthor(AuthorDto dto);

    @Mapping(target = "bookIds", source = "books", qualifiedByName = "extractBookIds")
    @Mapping( target = "authorName", expression = "java(entity.getFirstName() + \" \" + entity.getLastName())")
    public abstract AuthorDto toDto(Author entity);

    @Named("extractBookIds")
    public List<Long> extractBookIds(List<Book> books) {
        return books.stream()
                .map(Book::getId)
                .collect(Collectors.toList());
    }


}