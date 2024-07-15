package com.pet.Bookshop.service.kafka;

import com.pet.Bookshop.entity.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {

    @KafkaListener(topics = "BookTopic", groupId = "bookshop-consumer")
    public void consume(Book book) {
        log.info("Получили книгу через kafka: {}", book);
    }
}