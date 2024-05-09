package com.miguelangel.supermarketDataCollector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan("com.miguelangel")
@EnableScheduling
public class SupermarketDataCollectorApplication{
	public static void main(String[] args) {
		SpringApplication.run(SupermarketDataCollectorApplication.class, args);
		
	}
}
