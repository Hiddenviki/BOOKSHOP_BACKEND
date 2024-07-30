package com.pet.Bookshop.mapper;

import com.pet.Bookshop.dto.AuthorDto;
import com.pet.Bookshop.dto.NewAuthorDto;
import com.pet.Bookshop.entity.Author;
import com.pet.Bookshop.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;


@Mapper
public abstract class AuthorMapper {
    @Mapping(target = "id", expression = "java(dto.getId())")
    @Mapping(target = "firstName", expression = "java(dto.getFirstName())")
    @Mapping(target = "lastName", expression = "java(dto.getLastName())")
    public abstract Author toAuthor(NewAuthorDto dto);

    @Mapping(target = "bookIds", source = "books", qualifiedByName = "extractBookIds")
    @Mapping(target = "authorName", expression = "java(entity.getFirstName() + \" \" + entity.getLastName())")
    public abstract AuthorDto toDto(Author entity);

    @Named("extractBookIds")
    public List<Long> extractBookIds(List<Book> books) {
        return books.stream()
                .map(Book::getId).toList();
    }
}