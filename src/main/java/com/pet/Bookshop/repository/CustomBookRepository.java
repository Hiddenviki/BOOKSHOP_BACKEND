package com.pet.Bookshop.repository;

import com.pet.Bookshop.model.entity.Book;
import com.pet.Bookshop.model.filter.BookFilter;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomBookRepository{
    List<Book> findByFilter(BookFilter filter);
}
