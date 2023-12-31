package com.kai.mynote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
public class MyNoteApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyNoteApplication.class, args);
	}

}