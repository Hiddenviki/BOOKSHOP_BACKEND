package com.pet.Bookshop.service;

import com.pet.Bookshop.mapper.AuthorMapper;
import com.pet.Bookshop.model.dto.AuthorDto;
import com.pet.Bookshop.model.entity.Author;
import com.pet.Bookshop.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public List<AuthorDto> getAllAuthors() {
        log.info("AuthorService-getAllAuthors: Смотрим на всех авторов");
        //беру всё из репозитория
        List<Author> authors = authorRepository.findAll();
        //пробегаюсь по листу и делаю из него ДТО а потом опять делаю лист
        return authors.stream()
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
        log.info("AuthorService-getAuthorById: id " + id);

        return authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Не нашли автора с таким айди: " + id));

    }


    @Transactional
    public AuthorDto createAuthor(Author author) {
        if (author.getFirstName() == null || author.getLastName() == null) {
            throw new IllegalArgumentException("Имя и фамилия автора не могут быть null");
        }

        log.info("AuthorService-createAuthor: Создание нового автора " + author.getLastName() + " " + author.getFirstName());

        Author savedAuthor = authorRepository.save(author); // Сохранение автора в базу данных

        log.info("AuthorService-createAuthor: Автор успешно создан с ID: " + savedAuthor.getId());

        return authorMapper.toDto(savedAuthor); // Возвращаем DTO сохраненного автора
    }




}
