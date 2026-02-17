package com.fuji.Evaluator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.resilience.annotation.EnableResilientMethods;

@SpringBootApplication
@EnableResilientMethods
public class EvaluatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(EvaluatorApplication.class, args);
	}

}
