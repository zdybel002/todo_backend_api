package ru.javabegin.backend.todo.todobackend.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Dla wszystkich ścieżek
                .allowedOrigins("http://localhost:3000") // Tylko localhost:3000
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Dozwolone metody HTTP
                .allowCredentials(true); // Zezwolenie na przesyłanie ciasteczek i nagłówków autoryzacji
    }
}
