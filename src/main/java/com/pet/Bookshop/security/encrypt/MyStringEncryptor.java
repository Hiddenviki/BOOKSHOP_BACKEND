package com.pet.Bookshop.security.encrypt;

public interface MyStringEncryptor {
    String encrypt(String text);

    String decrypt(String encryptedText);
}