package com.pet.Bookshop.config;


import com.pet.Bookshop.security.encrypt.FakeStringEncryptor;
import com.pet.Bookshop.security.encrypt.MyStringEncryptor;
import com.pet.Bookshop.security.encrypt.RealStringEncryptor;
import io.github.cdimascio.dotenv.Dotenv;
import org.jasypt.hibernate5.encryptor.HibernatePBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.salt.RandomSaltGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBEncryptConfig {
    // Шифрование для базы данных
    @Bean
    @ConditionalOnExpression("${encryption.enabled} == true")
    HibernatePBEStringEncryptor hibernateEncryptor() {
        HibernatePBEStringEncryptor encryptor = new HibernatePBEStringEncryptor();
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

    // Реальный шифровальщик строк, если encryption.enabled = true
    @Bean
    @ConditionalOnExpression("${encryption.enabled} == true")
    MyStringEncryptor realStringEncryptor(HibernatePBEStringEncryptor hibernateEncryptor) {
        return new RealStringEncryptor(hibernateEncryptor);
    }

    // Фейковый шифровальщик строк, если encryption.enabled = false
    @Bean
    @ConditionalOnExpression("${encryption.enabled} == false")
    MyStringEncryptor fakeStringEncryptor() {
        return new FakeStringEncryptor();
    }
}
