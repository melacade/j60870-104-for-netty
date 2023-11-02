package com.melody.j60870;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class OneZeroFourApplication {
	
	
	public static void main(String[] args) {
//		SpringApplication.run(OneZeroFourApplication.class, args);

		try {
			Server.builder().build().start();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
}
