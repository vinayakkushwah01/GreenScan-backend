package com.greenscan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins = {
    "http://localhost:3000", // React
    "http://localhost:4000"  // Flutter
})
public class GreenscanApplication {

	public static void main(String[] args) {
		SpringApplication.run(GreenscanApplication.class, args);
	}

}
