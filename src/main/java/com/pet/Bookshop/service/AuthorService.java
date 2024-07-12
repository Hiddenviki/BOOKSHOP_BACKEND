package com.pet.Bookshop.service;

import com.pet.Bookshop.dto.AuthorDto;
import com.pet.Bookshop.dto.NewAuthorDto;
import com.pet.Bookshop.entity.Author;
import com.pet.Bookshop.mapper.AuthorMapper;
import com.pet.Bookshop.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public List<AuthorDto> getAllAuthors() {
        log.info("AuthorService-getAllAuthors: Смотрим на всех авторов");
        //пробегаюсь по листу и делаю из него ДТО а потом опять делаю лист
        return authorRepository.findAll().stream()
                .map(authorMapper::toDto)
                .collect(Collectors.toList());
    }

    public AuthorDto getAuthorById(Long id) {
        log.info("AuthorService-getAuthorById: показан автор с id " + id);
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Не нашли автора с таким айди: " + id));
        //перед возвратом делаю ДТО
        return authorMapper.toDto(author);
    }

    public Author getAuthor(Long id) {
        log.info("AuthorService-getAuthor: id " + id);

        return authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Не нашли автора с таким айди: " + id));

    }


    @Transactional
    public AuthorDto createAuthor(NewAuthorDto author) {
        if (author.getFirstName() == null || author.getLastName() == null) {
            throw new IllegalArgumentException("Имя и фамилия автора не могут быть null");
        }

        log.info("AuthorService-createAuthor: Создание нового автора " + author.getLastName() + " " + author.getFirstName());

        Author newAuthor = authorMapper.toAuthor(author); // Преобразование DTO в Author
        authorRepository.save(newAuthor); // Сохранение автора в базу данных

        log.info("AuthorService-createAuthor: Автор успешно создан с ID: " + newAuthor.getId());

        return authorMapper.toDto(newAuthor); // Возвращаем DTO сохраненного автора
    }




}
