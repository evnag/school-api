package ru.hogwarts.schoolapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class SchoolApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolApiApplication.class, args);
	}

}
