package com.pet.Bookshop.mapper;

import com.pet.Bookshop.model.dto.SignInDto;
import com.pet.Bookshop.model.dto.SignUpDto;
import com.pet.Bookshop.model.entity.User;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Mapper
public abstract class UserMapper {

    @Autowired
    protected BCryptPasswordEncoder bCryptPasswordEncoder;

    //вход возвращает дто
    @Mapping(target = "password", ignore = true) //ну пароль не дадим фронту
    @Mapping(target = "login", source = "login")
    @Mapping(target = "id", expression = "java(user.getId())")
    @Mapping(target = "email", expression = "java(user.getEmail())")
//    @Mapping(target = "role", expression = "java(com.pet.Bookshop.model.enums.Role.USER)")
    public abstract SignInDto toSignInDtoFromUser(User user);

    //регистрация из дто в user
    @Mapping(target = "email", source = "email")
    @Mapping(target = "login", source = "login")
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "role", expression = "java(com.pet.Bookshop.model.enums.Role.GUEST)")
    public abstract User toUserFromSignUpDto(SignUpDto dto);

    //перед вызовом мапинга устанавливаем захешированый пароль
    @BeforeMapping
    protected void setPassword(SignUpDto dto, @MappingTarget User user) {
        if (dto.isPasswordsMatch()) {
            user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword1()));
        }
    }

}
