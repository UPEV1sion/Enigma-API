package org;

import org.nativeCInterface.NativeInterfaceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
@ComponentScan({"org.db", "org.nativeCInterface", "org.api"})
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@EnableCaching
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
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/api/**")  // Match all paths under /api/
                        .allowedOrigins("http://localhost:8080", "https://enigma-zyklometer.rwu.de")//localhost:80")
                        .allowedMethods("POST")  // You can restrict allowed methods if needed
                        .allowedHeaders("*");  // Allow all headers
            }
        };
        }
}