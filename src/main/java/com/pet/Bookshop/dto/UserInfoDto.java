package com.pet.Bookshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserInfoDto {

    private Long id;
    private String login;
    private String email;
    private String role;
    private LocalDateTime createdDate;
    private List<BookDto> bookDto;

}
