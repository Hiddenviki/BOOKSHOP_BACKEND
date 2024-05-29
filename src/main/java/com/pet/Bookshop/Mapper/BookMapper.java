package com.pet.Bookshop.Mapper;
import com.pet.Bookshop.Entity.Book;
import com.pet.Bookshop.DTO.BookDto;
import org.mapstruct.Mapper;

@Mapper
public interface BookMapper {

    Book toBook(BookDto bookDto);

    BookDto toDto(Book book);
}
