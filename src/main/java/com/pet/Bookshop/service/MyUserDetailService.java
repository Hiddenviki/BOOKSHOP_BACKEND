package com.pet.Bookshop.service;

import com.pet.Bookshop.dto.BookDto;
import com.pet.Bookshop.dto.UserInfoDto;
import com.pet.Bookshop.entity.Author;
import com.pet.Bookshop.mapper.BookMapper;
import com.pet.Bookshop.mapper.UserInfoMapper;
import com.pet.Bookshop.repository.AuthorRepository;
import com.pet.Bookshop.repository.UserRepository;
import com.pet.Bookshop.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserInfoMapper userInfoMapper;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username)
                .map(MyUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User с таким логином не нашелся " + username));
    }

    public UserInfoDto showUserInfo() {
        //загружаем юзера
        UserDetails userDetails = getCurrentUser();
        return getUserInfoDto(userDetails); //нужно ли сюда передвать userDetails в аргумент?
    }

    private UserInfoDto getUserInfoDto(UserDetails userDetails) {

        if(!(userDetails instanceof MyUserDetails myUserDetails)) {
            throw new RuntimeException("Ошибка при получении информации о пользователе getUserInfoDto");
        }

        final var user = myUserDetails.getUser();

        UserInfoDto userInfoDto = userInfoMapper.toUserInfoDto(user);
        userInfoDto.setBookDto(getBooksFromAuthor(userInfoDto));

        return userInfoDto;

    }

    private List<BookDto> getBooksFromAuthor(UserInfoDto user) {
        if (isAuthor(user.getId())) {
            return authorRepository.findById(user.getId())
                    .map(Author::getBooks)
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(bookMapper::toDto)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    //является ли пользователь автором
    private boolean isAuthor(Long id) {
        return authorRepository.existsById(id);
    }

    //метод с jwt информацией
    private UserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (UserDetails) authentication.getPrincipal();
    }

}
