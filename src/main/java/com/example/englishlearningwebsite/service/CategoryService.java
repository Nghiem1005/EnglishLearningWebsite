package com.example.englishlearningwebsite.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface CategoryService {
  ResponseEntity<?> getLevel();
  ResponseEntity<?> getCourseType();

}
