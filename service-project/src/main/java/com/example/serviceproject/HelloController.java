package com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;

@RestController
public class HelloController {
	
	@Value("${also.my.value}")
	private String myValue;

	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot! " + myValue;
	}

}