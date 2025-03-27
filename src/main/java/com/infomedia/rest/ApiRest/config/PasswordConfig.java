package com.infomedia.rest.ApiRest.config;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class PasswordConfig {

    @Bean
    public StrongPasswordEncryptor passwordEncryptor() {
        return new StrongPasswordEncryptor();
    }
}