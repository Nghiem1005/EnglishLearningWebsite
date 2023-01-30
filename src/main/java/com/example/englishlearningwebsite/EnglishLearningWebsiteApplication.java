package com.example.englishlearningwebsite;

import com.example.englishlearningwebsite.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class EnglishLearningWebsiteApplication {

  public static void main(String[] args) {
    SpringApplication.run(EnglishLearningWebsiteApplication.class, args);
  }

}
