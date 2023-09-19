package com.example.mybookshopapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Value("${upload.path.book-covers}")
    private String uploadBookCoverPath;
    @Value("${upload.path.author-photos}")
    private String uploadAuthorPhotoPath;

    @Value("${download.path.books}")
    private String uploadBookPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String locationPrefix= "file:";
        registry.addResourceHandler("/book-covers/**").addResourceLocations( locationPrefix+ uploadBookCoverPath + "/");
        registry.addResourceHandler("/author-photos/**").addResourceLocations(locationPrefix + uploadAuthorPhotoPath + "/");
        registry.addResourceHandler("/book-files/**").addResourceLocations(locationPrefix + uploadBookPath + "/");
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
