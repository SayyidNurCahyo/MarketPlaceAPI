package com.enigma.marketplace.MarketPlaceAPI;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SecurityScheme(
		name = "Bearer Authentication",
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT",
		scheme = "bearer"
)
public class MarketPlaceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketPlaceApiApplication.class, args);
	}

}
