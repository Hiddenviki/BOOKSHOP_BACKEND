package com.pet.Bookshop.mapper;

import com.pet.Bookshop.dto.SignInDto;
import com.pet.Bookshop.dto.SignUpDto;
import com.pet.Bookshop.entity.User;
import com.pet.Bookshop.enums.Roles;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.reflect.Whitebox;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void testToSignInDtoFromUser() {
        // Данные
        User user = new User();
        user.setId(1L);
        user.setLogin("testuser");
        user.setEmail("test@example.com");

        SignInDto expectedSignInDto = new SignInDto(1L, "test@example.com", Roles.GUEST, "testuser", "password!");

        // Вызов метода
        SignInDto actualSignInDto = userMapper.toSignInDtoFromUser(user);

        // Проверки
        assertEquals(expectedSignInDto.getId(), actualSignInDto.getId());
        assertEquals(expectedSignInDto.getLogin(), actualSignInDto.getLogin());
        assertEquals(expectedSignInDto.getEmail(), actualSignInDto.getEmail());
    }

    @Test
    void testToUserFromSignUpDto() {
        // Данные
        SignUpDto signUpDto = new SignUpDto("test@example.com", "testuser", "password!", "password!");

        User expectedUser = new User();
        expectedUser.setLogin("testuser");
        expectedUser.setEmail("test@example.com");
        expectedUser.setActive(true);
        expectedUser.setRole(Roles.GUEST);

        // Устанавливаем мок в UserMapper
        Whitebox.setInternalState(userMapper, bCryptPasswordEncoder);

        // Вызов метода
        User actualUser = userMapper.toUserFromSignUpDto(signUpDto);

        // Проверки
        assertEquals(expectedUser.getLogin(), actualUser.getLogin());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertTrue(actualUser.isActive());
        assertEquals(expectedUser.getRole(), actualUser.getRole());
    }

    @Test
    void testSetPassword() {
        // Данные
        SignUpDto signUpDto = new SignUpDto("test@example.com", "testuser", "password!", "password!");
        User user = new User();

        when(bCryptPasswordEncoder.encode("password!")).thenReturn("$2y$08$OS3WMrVEW3iGkTT9Kuhj2em68NIdwRuNzspHiUeref7BFory/DOFi");

        // Устанавливаем мок в UserMapper
        Whitebox.setInternalState(userMapper, bCryptPasswordEncoder);

        // Вызов метода
        userMapper.setPassword(signUpDto, user);

        // Проверка
        assertEquals("$2y$08$OS3WMrVEW3iGkTT9Kuhj2em68NIdwRuNzspHiUeref7BFory/DOFi", user.getPassword());
    }
}