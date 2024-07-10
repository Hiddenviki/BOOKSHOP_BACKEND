package com.pet.Bookshop.api;

import com.pet.Bookshop.dto.AuthorDto;
import com.pet.Bookshop.entity.Author;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Author API", description = "API для работы с авторами")
public interface AuthorApi {

    @Operation(
            description = "Возвращает список всех авторов",
            summary = "Возвращает список всех авторов"
    )
    List<AuthorDto> getAllAuthors();

    @Operation(
            description = "Возвращает автора по его id",
            summary = "Возвращает автора по его id"
    )
    AuthorDto getAuthorById(@PathVariable @Parameter(description = "id пользователя")
                            @io.swagger.v3.oas.annotations.media.Schema(example = "{\"id\": 1}") Long id);

    @Operation(
            description = "Создает нового автора",
            summary = "Создает нового автора"
    )
    AuthorDto createAuthor(
            @RequestBody @Parameter(description = "Сущнось автора")
            @io.swagger.v3.oas.annotations.media.Schema(
                    example = "{\"id\": 1, \"firstName\": \"Лев\", \"lastName\": \"Толстой\"}") Author author);
}
