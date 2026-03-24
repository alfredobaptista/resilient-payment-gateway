package com.github.alfredobaptista;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ResilientPaymentGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResilientPaymentGatewayApplication.class, args);
	}

}
