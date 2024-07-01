package com.pet.Bookshop.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class TokenDto {
    private String jwtToken;

    public TokenDto(String newToken) {
        this.jwtToken = newToken;
    }
}
