package com.pet.Bookshop.api;

import com.pet.Bookshop.dto.BookDto;
import com.pet.Bookshop.dto.filter.BookFilterDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Book API", description = "API для работы с книгами")
public interface BookApi {
    @Operation(
            description = "Возвращает список всех книг",
            summary = "Возвращает список всех книг"
    )
    List<BookDto> getBooks();

    @Operation(
            description = "Возвращает информацию о книге по ее id",
            summary = "Возвращает информацию о книге по ее id"
    )
    BookDto getBookById(@Parameter(description = "id книги") Long id);

    @Operation(
            description = "Создаёт книгу",
            summary = "Создаёт книгу"
    )
    BookDto createBook(@Parameter(description = "DTO книги") BookDto bookDto);

    @Operation(
            description = "Удаляет книгу по ее id",
            summary = "Удаляет книгу по ее id"
    )
    void deleteBookById(@Parameter(description = "id книги") Long id);

    @Operation(
            description = "Редактирование информации о книге по ее id",
            summary = "Редактирование информации о книге по ее id"
    )
    BookDto editBookById(@Parameter(description = "измененное DTO книги") BookDto bookDto);

    @Operation(
            description = "Поиск книг по фильтрам",
            summary = "Поиск книг по фильтрам"
    )
    List<BookDto> filter(@Parameter(description = "DTO фильтра") BookFilterDto filter);
}
