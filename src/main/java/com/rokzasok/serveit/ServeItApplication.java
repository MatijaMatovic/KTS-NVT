package com.rokzasok.serveit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/*
 * TODO: izbrisati '(exclude = {SecurityAutoConfiguration.class })'
 *   kad se implementira Spring security OBAVEZNO
 */
@SpringBootApplication
public class ServeItApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServeItApplication.class, args);
    }

}
