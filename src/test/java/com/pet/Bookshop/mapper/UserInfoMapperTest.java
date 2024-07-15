package com.pet.Bookshop.mapper;

import com.pet.Bookshop.dto.UserInfoDto;
import com.pet.Bookshop.entity.User;
import com.pet.Bookshop.enums.Roles;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserInfoMapperTest {

    private UserInfoMapper userInfoMapper = Mappers.getMapper(UserInfoMapper.class);

    @Test
    void testToUserInfoDto() {
        // Создаем тестового пользователя
        User user = new User();
        user.setId(1L);
        user.setLogin("testuser");
        user.setEmail("test@example.com");
        user.setRole(Roles.GUEST);
        user.setCreatedDate(LocalDateTime.of(2024, 7, 12, 13, 45, 0));
        user.setActive(true);

        UserInfoDto expectedUserInfoDto = new UserInfoDto
                (
                        1L, "testuser", "test@example.com",
                        Roles.GUEST.toString(),
                        LocalDateTime.of(2024, 7, 12, 13, 45, 0),
                        Collections.emptyList()
                );

        // Вызываем метод
        UserInfoDto userInfoDto = userInfoMapper.toUserInfoDto(user);

        // Проверяем
        assertEquals(expectedUserInfoDto.getId(), userInfoDto.getId());
        assertEquals(expectedUserInfoDto.getLogin(), userInfoDto.getLogin());
        assertEquals(expectedUserInfoDto.getEmail(), userInfoDto.getEmail());
        assertEquals(expectedUserInfoDto.getRole(), userInfoDto.getRole());
        assertEquals(expectedUserInfoDto.getCreatedDate(), userInfoDto.getCreatedDate());
    }
}