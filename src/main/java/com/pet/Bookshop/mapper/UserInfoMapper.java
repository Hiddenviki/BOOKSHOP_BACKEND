package com.pet.Bookshop.mapper;

import com.pet.Bookshop.dto.UserInfoDto;
import com.pet.Bookshop.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserInfoMapper {
    UserInfoDto toUserInfoDto(User user);
}