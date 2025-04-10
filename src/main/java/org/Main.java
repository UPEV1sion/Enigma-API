package org;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
@ComponentScan({"org.db", "org.nativeCInterface", "org.api"})
public class Main {
    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/enigma").allowedOrigins("http://localhost:8080");
                registry.addMapping("/cyclometer").allowedOrigins("http://localhost:8080");
                registry.addMapping("/catalogue").allowedOrigins("http://localhost:8080");
            }
        };
        }
}