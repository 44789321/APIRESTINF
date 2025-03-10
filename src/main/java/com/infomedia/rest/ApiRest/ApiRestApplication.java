package com.infomedia.rest.ApiRest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.infomedia.rest.ApiRest.config")
public class ApiRestApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ApiRestApplication.class, args);
	}

}
