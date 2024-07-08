package com.pet.Bookshop.mapper;

import com.pet.Bookshop.dto.UserInfoDto;
import com.pet.Bookshop.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserInfoMapper {

    @Mapping(target = "login", source = "login")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "role", expression = "java(String.valueOf(com.pet.Bookshop.enums.Roles.GUEST))")
    @Mapping(target = "createdDate", source = "createdDate")
    UserInfoDto toUserInfoDto(User user);

}