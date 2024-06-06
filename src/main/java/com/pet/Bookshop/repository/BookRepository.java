package com.pet.Bookshop.repository;

import com.pet.Bookshop.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends JpaRepository<Book, Long>, CustomBookRepository, JpaSpecificationExecutor<Book> {

}
