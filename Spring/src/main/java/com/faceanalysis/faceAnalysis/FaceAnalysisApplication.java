package com.faceanalysis.faceAnalysis;

import com.faceanalysis.faceAnalysis.storage.StorageProperties;
import com.faceanalysis.faceAnalysis.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(StorageProperties.class)
public class FaceAnalysisApplication {

	public static void main(String[] args) {
		SpringApplication.run(FaceAnalysisApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer(){
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/*").allowedOrigins("*").allowedMethods("*");

				registry.addMapping("/admin/*").allowedOrigins("*").allowedMethods("*");

				registry.addMapping("/files/admin/*").allowedOrigins("*").allowedMethods("*");

				registry.addMapping("/files/*").allowedOrigins("*").allowedMethods("*");

			}
		};
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.init();
		};
	}

}
