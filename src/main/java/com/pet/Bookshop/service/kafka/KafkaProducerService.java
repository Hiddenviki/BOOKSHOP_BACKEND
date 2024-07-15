package com.pet.Bookshop.service.kafka;

import com.pet.Bookshop.entity.Book;
import com.pet.Bookshop.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {
    private final KafkaTemplate<String, Book> kafkaTemplate;
    private final BookRepository bookRepository;

    public void sendMessage(Book newBookObject) {

        kafkaTemplate.send("BookTopic", newBookObject);
        bookRepository.save(newBookObject); //сохраняю книгу
        log.info("KafkaProducerService-sendMessage: Книга создана успешно");

    }
}
