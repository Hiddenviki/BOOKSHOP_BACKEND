package com.pet.Bookshop.security;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@NoArgsConstructor
@Component
public class UUIDGenerator {
    //генерация uuid на основе логина из дто
    public UUID generateUUIDFromValue(String login) {
        byte[] bytes = login.getBytes(StandardCharsets.UTF_8);
        return UUID.nameUUIDFromBytes(bytes);
    }
}