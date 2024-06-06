package com.pet.Bookshop.repository;

import com.pet.Bookshop.model.entity.Book;
import com.pet.Bookshop.model.filter.BookFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CustomBookRepositoryImpl implements CustomBookRepository {

    private final EntityManager entityManager;

    public CustomBookRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Book> findByFilter(BookFilter filter) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);
        Root<Book> bookRoot = criteriaQuery.from(Book.class);

        List<Predicate> predicates = new ArrayList<>();

//        String name = filter.getName();
//        if (name != null) predicates.add(criteriaBuilder.like(bookRoot.get("name"), "%" + name + "%"));

        Optional.ofNullable(filter.getName())
                .ifPresent(name -> predicates.add(criteriaBuilder.like(bookRoot.get("name"), "%" + name + "%")));

        Optional.ofNullable(filter.getBrand())
                .ifPresent(brand -> predicates.add(criteriaBuilder.like(bookRoot.get("brand"), "%" + brand + "%")));

        Optional.ofNullable(filter.getCover())
                .ifPresent(cover -> predicates.add(criteriaBuilder.like(bookRoot.get("cover"), "%" + cover + "%")));

        Optional.ofNullable(filter.getAuthorFirstName())
                .ifPresent(firstName -> predicates.add(criteriaBuilder.like(bookRoot.get("author").get("firstName"), "%" + firstName + "%")));

        Optional.ofNullable(filter.getAuthorLastName())
                .ifPresent(lastName -> predicates.add(criteriaBuilder.like(bookRoot.get("author").get("lastName"), "%" + lastName + "%")));

        Optional.ofNullable(filter.getCount())
                .ifPresent(count -> predicates.add(criteriaBuilder.equal(bookRoot.get("count"), count)));

        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        Predicate orPredicate = criteriaBuilder.and(predicateArray);
        criteriaQuery.where(orPredicate);

        TypedQuery<Book> query = entityManager.createQuery(criteriaQuery);

        return query.getResultList();
    }
}