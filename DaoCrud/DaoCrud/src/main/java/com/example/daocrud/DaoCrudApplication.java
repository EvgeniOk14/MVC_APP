package com.example.daocrud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class DaoCrudApplication implements WebMvcConfigurer {

    public static void main(String[] args)
    {
        SpringApplication.run(DaoCrudApplication.class, args);
    }

}
