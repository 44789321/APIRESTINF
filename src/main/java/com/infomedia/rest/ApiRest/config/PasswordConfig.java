package com.infomedia.rest.ApiRest.config;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PasswordConfig {

    @Bean
    public StrongPasswordEncryptor passwordEncryptor() {
        return new StrongPasswordEncryptor();
    }
}