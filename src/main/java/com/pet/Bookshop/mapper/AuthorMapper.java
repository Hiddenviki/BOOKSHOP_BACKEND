package com.pet.Bookshop.mapper;

import com.pet.Bookshop.model.dto.AuthorDto;
import com.pet.Bookshop.model.entity.Author;
import com.pet.Bookshop.model.entity.Book;
import org.mapstruct.*;

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