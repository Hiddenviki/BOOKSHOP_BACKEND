package com.pet.Bookshop.security.encrypt;


import lombok.RequiredArgsConstructor;
import org.jasypt.hibernate5.encryptor.HibernatePBEStringEncryptor;

@RequiredArgsConstructor
public class RealStringEncryptor implements MyStringEncryptor {
    private final HibernatePBEStringEncryptor encryptor;

    @Override
    public String encrypt(String text) {
        return encryptor.encrypt(text);
    }

    @Override
    public String decrypt(String encryptedText) {
        return encryptor.decrypt(encryptedText);
    }
}
