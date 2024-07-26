package com.pet.Bookshop.security.encrypt;

public class FakeStringEncryptor implements MyStringEncryptor {
    @Override
    public String encrypt(String text) {
        return text;
    }

    @Override
    public String decrypt(String encryptedText) {
        return encryptedText;
    }
}