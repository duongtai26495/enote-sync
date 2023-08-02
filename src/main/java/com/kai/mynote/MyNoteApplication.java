package com.kai.mynote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
public class MyNoteApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyNoteApplication.class, args);


	}

}