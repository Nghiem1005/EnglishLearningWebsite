package com.example.englishlearningwebsite.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
  @Value("${app.cors.allowedOrigins}")
  private String[] allowedOrigins;
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")  // allow cros path
        .allowedOriginPatterns("*")    // allow cros origin
        .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")// allow method
        .maxAge(168000) //
        .allowedHeaders("*")
        .allowCredentials(true);
  }
}
