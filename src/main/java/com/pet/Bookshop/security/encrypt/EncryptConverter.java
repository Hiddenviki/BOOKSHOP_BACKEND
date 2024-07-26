package com.pet.Bookshop.security.encrypt;


import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;

//для данных в БД
@Converter
@RequiredArgsConstructor
public class EncryptConverter implements AttributeConverter<String, String> {

    private final MyStringEncryptor stringEncryptor;

    @Override
    public String convertToDatabaseColumn(String s) {
        return stringEncryptor.encrypt(s);
    }

    @Override
    public String convertToEntityAttribute(String s) {
        return stringEncryptor.decrypt(s);
    }
}