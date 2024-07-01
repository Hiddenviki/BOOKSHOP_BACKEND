package com.pet.Bookshop.security.userdetails;

import com.pet.Bookshop.model.entity.Book;
import com.pet.Bookshop.model.entity.User;
import com.pet.Bookshop.repository.AuthorRepository;
import com.pet.Bookshop.repository.BookRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class MyUserDetails implements UserDetails {
    private final User user;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(this.user.getRole().toString());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.user.isActive();
    }

    //является ли пользователь автором
    public boolean isAuthor(Long id) {
        return authorRepository.existsById(id);
    }

    //список книг автора (book.toString())
    public String getBooksInfo(Long id) {
        List<String> bookToString = bookRepository.findAllByAuthorId(id)
                .stream()
                .map(Book::toString)
                .collect(Collectors.toList());
        return String.join(",\n ", bookToString);
    }

    @Override
    public String toString() {
        String userDetails = "MyUserDetails {" +
                "\nId: " + user.getId() +
                "\nLogin: " + user.getLogin() +
                "\nEmail: " + user.getEmail() +
                "\nRole: " + user.getRole() +
                "\nDate of creation: " + user.getCreatedDate() +
                "\n}";

        if (isAuthor(user.getId())) {
            userDetails += "\nBooks: " + getBooksInfo(user.getId());
        }

        return userDetails;
    }
}
