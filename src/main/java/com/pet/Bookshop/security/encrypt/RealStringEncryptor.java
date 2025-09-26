package com.pet.Bookshop.security.encrypt;


import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

@RequiredArgsConstructor
public class RealStringEncryptor implements MyStringEncryptor {
    private final StandardPBEStringEncryptor encryptor;

    @Override
    public String encrypt(String text) {
        return encryptor.encrypt(text);
    }

    @Override
    public String decrypt(String encryptedText) {
        return encryptor.decrypt(encryptedText);
    }
}
