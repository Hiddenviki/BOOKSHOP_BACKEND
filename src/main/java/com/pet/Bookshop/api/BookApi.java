package com.pet.Bookshop.api;

import com.pet.Bookshop.dto.BookDto;
import com.pet.Bookshop.dto.filter.BookFilterDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

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
    BookDto getBookById(@PathVariable @Parameter(description = "id книги")
                        @io.swagger.v3.oas.annotations.media.Schema(
            example = "{\"id\": 1}") Long id);

    @Operation(
            description = "Создаёт книгу",
            summary = "Создаёт книгу"
    )
    BookDto createBook(
            @Valid @RequestBody @Parameter(description = "DTO книги")
            @io.swagger.v3.oas.annotations.media.Schema(
                    example = "{\"name\": \"Война и Мир\", \"brand\": \"Просвящение\", \"cover\": \"SOFT\", \"authorId\": 1, \"count\": 12}")
            BookDto bookDto
    );
    @Operation(
            description = "Удаляет книгу по ее id",
            summary = "Удаляет книгу по ее id"
    )
    void deleteBookById(@PathVariable @Parameter(description = "id книги")
                        @io.swagger.v3.oas.annotations.media.Schema(
                                example = "{\"id\": 1}") Long id);

    @Operation(
            description = "Редактирование информации о книге по ее id",
            summary = "Редактирование информации о книге по ее id"
    )
    BookDto editBookById(@Valid @RequestBody @Parameter(description = "измененное DTO книги")
                         @io.swagger.v3.oas.annotations.media.Schema(
                                 example = "{\"name\": \"Война и Мир Часть 3\", \"brand\": \"Просвящение\", \"cover\": \"SOFT\", \"authorId\": 1, \"count\": 12}")
                         BookDto bookDto
    );

    @Operation(
            description = "Поиск книг по фильтрам",
            summary = "Поиск книг по фильтрам"
    )
    List<BookDto> filter(@RequestBody @Parameter(description = "DTO фильтра")
                         @io.swagger.v3.oas.annotations.media.Schema(
                                 example = "{\"name\": \"Война и Мир\"}") BookFilterDto filter);

}
