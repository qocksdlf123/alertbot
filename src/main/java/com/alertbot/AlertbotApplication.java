package com.alertbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class AlertbotApplication {

	public static void main(String[] args) throws IOException {
		ConfigurableApplicationContext context = SpringApplication.run(AlertbotApplication.class, args);
		BithumbParsingService bithumbParsingService = context.getBean(BithumbParsingService.class);
		bithumbParsingService.parseBithumb();

	}

}
