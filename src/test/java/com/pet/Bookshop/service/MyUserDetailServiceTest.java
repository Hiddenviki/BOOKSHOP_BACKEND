package com.pet.Bookshop.service;

import com.pet.Bookshop.dto.UserInfoDto;
import com.pet.Bookshop.entity.Author;
import com.pet.Bookshop.entity.User;
import com.pet.Bookshop.enums.Roles;
import com.pet.Bookshop.mapper.BookMapper;
import com.pet.Bookshop.mapper.UserInfoMapper;
import com.pet.Bookshop.repository.AuthorRepository;
import com.pet.Bookshop.repository.UserRepository;
import com.pet.Bookshop.security.MyUserDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MyUserDetailServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserInfoMapper userInfoMapper;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    MyUserDetailService myUserDetailService;

    @Test
    void testLoadUserByUsername() {
        // Создание тестовых данных
        User user = new User();
        user.setId(1L);
        user.setLogin("testuser");
        user.setEmail("testuser@example.com");
        user.setRole(Roles.GUEST);

        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));

        // Вызов тестируемого метода
        UserDetails userDetails = myUserDetailService.loadUserByUsername(user.getLogin());

        // Проверка результатов
        assertEquals(user.getLogin(), userDetails.getUsername());
    }

    @Test //приватные методы не проверяются
    void testShowUserInfo() {
        // Подготовка тестовых данных
        User testUser = new User();
        testUser.setId(1L);
        testUser.setLogin("testuser");
        testUser.setEmail("testuser@example.com");
        testUser.setActive(true);
        testUser.setRole(Roles.GUEST);
        LocalDateTime date = LocalDateTime.now();
        testUser.setCreatedDate(date);

        MyUserDetails userDetails = new MyUserDetails(testUser);

        // Ожидаемый объект UserInfoDto
        UserInfoDto expectedUserInfoDto = new UserInfoDto(
                testUser.getId(),  // Assuming getId() returns Long
                testUser.getLogin(),
                testUser.getEmail(),
                String.valueOf(testUser.getRole()),
                testUser.getCreatedDate(),
                Collections.emptyList()  // Assuming the BookDto list is empty for this example
        );

        //поведение userRepository
        lenient().when(userRepository.findByLogin(testUser.getLogin())).thenReturn(Optional.of(testUser));

        // поведение userInfoMapper и authorRepository
        when(userInfoMapper.toUserInfoDto(testUser)).thenReturn(expectedUserInfoDto);
        when(authorRepository.existsById(testUser.getId())).thenReturn(true);
        when(authorRepository.findById(testUser.getId())).thenReturn(Optional.of(new Author()));

        // Моки SecurityContextHolder и Authentication
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Вызов тестируемого метода
        UserInfoDto userInfoDto = myUserDetailService.showUserInfo();

        // Проверка результатов
        assertEquals(testUser.getId(), userInfoDto.getId());
        assertEquals(testUser.getLogin(), userInfoDto.getLogin());
        assertEquals(testUser.getEmail(), userInfoDto.getEmail());
        assertEquals(testUser.getRole().toString(), userInfoDto.getRole());
        assertEquals(testUser.getCreatedDate(), userInfoDto.getCreatedDate());
        assertEquals(Collections.emptyList(), userInfoDto.getBookDto());
    }

}
