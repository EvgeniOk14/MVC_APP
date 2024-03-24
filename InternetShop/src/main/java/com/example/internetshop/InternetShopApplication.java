package com.example.internetshop;

import com.example.internetshop.controllers.ShopController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InternetShopApplication {
	public static void main(String[] args) {
		SpringApplication.run(ShopController.class, args);
	}
}
