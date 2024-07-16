package com.pet.Bookshop.service;

import com.pet.Bookshop.dto.BookDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final BookService bookService;

    @KafkaListener(topics = "BookTopic", groupId = "bookshop-consumer")
    public void consume(BookDto bookDto) {
        log.info("Получили книгу через kafka: \n{}", bookDto);
        //создаем книгу
        bookService.createBook(bookDto);
    }
}