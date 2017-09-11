package com.jwt.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class SpringbootJwtApplication {


    @RequestMapping("/")
    String hello(){
        return "hello world";
    }

	public static void main(String[] args) {
		SpringApplication.run(SpringbootJwtApplication.class, args);
	}
}
