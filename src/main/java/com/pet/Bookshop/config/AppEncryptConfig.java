package com.pet.Bookshop.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.salt.RandomSaltGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppEncryptConfig {
    // Шифрование для параметров из application.yml
    //испольует не мой интерфейс а jasypt.encryption.StringEncryptor
    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        Dotenv dotenv = Dotenv.configure().load();

        String jasyptAlgorithm = dotenv.get("JASYPT_ALGORITHM");
        encryptor.setAlgorithm(jasyptAlgorithm);

        String jasyptKey = dotenv.get("JASYPT_KEY");
        encryptor.setPassword(jasyptKey);

        encryptor.setIvGenerator(new RandomIvGenerator());
        encryptor.setSaltGenerator(new RandomSaltGenerator());
        encryptor.setKeyObtentionIterations(1000);

        return encryptor;
    }
}
