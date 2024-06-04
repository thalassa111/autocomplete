package com.me.autocomplete;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class autocompleteApplication {

	public static void main(String[] args) {
		SpringApplication.run(autocompleteApplication.class, args);
	}

}
