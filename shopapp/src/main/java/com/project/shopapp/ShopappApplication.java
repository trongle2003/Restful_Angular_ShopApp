package com.project.shopapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
@SpringBootApplication(exclude = {
 org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
 org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class,
 })
public class ShopappApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopappApplication.class, args);
	}

}
