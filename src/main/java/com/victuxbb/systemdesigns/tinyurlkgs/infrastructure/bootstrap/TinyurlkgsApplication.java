package com.victuxbb.systemdesigns.tinyurlkgs.infrastructure.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"com.victuxbb.systemdesigns.tinyurlkgs.infrastructure.api",
		"com.victuxbb.systemdesigns.tinyurlkgs.infrastructure.bootstrap"
})
public class TinyurlkgsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TinyurlkgsApplication.class, args);
	}

}
