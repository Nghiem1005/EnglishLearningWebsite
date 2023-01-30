package com.example.englishlearningwebsite.controller;

import com.example.englishlearningwebsite.service.CategoryService;
import com.example.englishlearningwebsite.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/category")
public class CategoryController {
  @Autowired private CategoryService categoryService;
  @GetMapping(value = "/courseType")
  public ResponseEntity<?> getAllCourseType() {
    return categoryService.getCourseType();
  }

  @GetMapping(value = "/level")
  public ResponseEntity<?> getLevel() {
    return categoryService.getLevel();
  }
}
