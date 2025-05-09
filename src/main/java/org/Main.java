package org;

import org.nativeCInterface.NativeInterfaceConfig;
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
        if (args.length > 0) {
            String argumentPath = args[0];
            System.setProperty("enigma.native.path", argumentPath);
        }
        System.out.println("Library Path: " + NativeInterfaceConfig.LIBRARY_PATH.toString());

        SpringApplication.run(Main.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/enigma").allowedOrigins("http://localhost:8080", "http://localhost:80", "https://localhost:8080", "https://localhost:80");
//                registry.addMapping("/cyclometer").allowedOrigins("http://localhost:8080", "http://localhost:80", "https://localhost:8080", "https://localhost:80");
//                registry.addMapping("/manualcyclometer").allowedOrigins("http://localhost:8080", "http://localhost:80", "https://localhost:8080", "https://localhost:80");
//                registry.addMapping("/catalogue").allowedOrigins("http://localhost:8080", "http://localhost:80", "https://localhost:8080", "https://localhost:80");
                registry.addMapping("/api/**")  // Match all paths under /api/
                        .allowedOrigins("http://localhost:8080")//localhost:80")
                        .allowedMethods("POST")  // You can restrict allowed methods if needed
                        .allowedHeaders("*");  // Allow all headers
            }
        };
        }
}