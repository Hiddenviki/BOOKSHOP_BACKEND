package com.pet.Bookshop.service;


import com.pet.Bookshop.dto.BookDto;
import com.pet.Bookshop.enums.Covers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerServiceTest {

    @Mock
    private BookService bookService;
    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;
    @Captor
    ArgumentCaptor<BookDto> bookDtoCaptor; //для проверки значений аргументов

    @Test
    void testConsume() {
        //Подготовка
        BookDto newBookDto = new BookDto(1L, "Война и Мир", "Просвящение", Covers.HARD, 1L, 10);
        BookDto expectedBookDto = new BookDto(1L, "Война и Мир", "Просвящение", Covers.HARD, 1L, 10);

        //поведение bookService.createBook для захвата параметров
        when(bookService.createBook(any(BookDto.class))).thenReturn(expectedBookDto);

        // Выполнение метода
        kafkaConsumerService.consume(newBookDto);

        // Проверка, что метод создания книги был вызван с правильными параметрами
        verify(bookService).createBook(bookDtoCaptor.capture());
        BookDto actualBookDto = bookDtoCaptor.getValue();

        // Проверка, что сохраненная книга равна ожидаемой
        assertEquals(expectedBookDto, actualBookDto);
    }
}
