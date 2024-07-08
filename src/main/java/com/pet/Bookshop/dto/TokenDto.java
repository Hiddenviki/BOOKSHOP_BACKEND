package com.pet.Bookshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenDto {
    private String jwtToken;

    public TokenDto(String newToken) {
        this.jwtToken = newToken;
    }
}
