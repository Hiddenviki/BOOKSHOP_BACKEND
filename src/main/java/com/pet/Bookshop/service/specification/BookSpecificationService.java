package com.pet.Bookshop.service.specification;

import com.pet.Bookshop.dto.filter.BookFilterDto;
import com.pet.Bookshop.entity.Author_;
import com.pet.Bookshop.entity.Book;
import com.pet.Bookshop.entity.Book_;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookSpecificationService {
    public Specification<Book> buildSpecification(BookFilterDto filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!StringUtils.isEmpty(filter.getName())) {
                //predicates.add(criteriaBuilder.like(root.get("name"), "%" + filter.getName() + "%"));
                predicates.add(criteriaBuilder.like(root.get(Book_.name), "%" + filter.getName() + "%"));

            }

            if (!StringUtils.isEmpty(filter.getBrand())) {
                predicates.add(criteriaBuilder.like(root.get(Book_.brand), "%" + filter.getBrand() + "%"));
            }

            if (filter.getCover() != null) {
                predicates.add(criteriaBuilder.like(root.get(String.valueOf(Book_.cover)), "%" + filter.getCover() + "%"));
            }

            if (filter.getCount() != null) {
                predicates.add(criteriaBuilder.equal(root.get(Book_.count), filter.getCount()));
            }

            if (!StringUtils.isEmpty(filter.getAuthorFirstName())) {
                //predicates.add(criteriaBuilder.like(root.get("author").get("firstName"), "%" + filter.getAuthorFirstName() + "%"));
                predicates.add(criteriaBuilder.like(root.get(Book_.author).get(Author_.firstName), "%" + filter.getAuthorFirstName() + "%"));
            }

            if (!StringUtils.isEmpty(filter.getAuthorLastName())) {
                //predicates.add(criteriaBuilder.like(root.get("author").get("lastName"), "%" + filter.getAuthorLastName() + "%"));
                predicates.add(criteriaBuilder.like(root.get(Book_.author).get(Author_.lastName), "%" + filter.getAuthorFirstName() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}