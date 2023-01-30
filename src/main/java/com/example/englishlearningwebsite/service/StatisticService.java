package com.example.englishlearningwebsite.service;

import org.springframework.http.ResponseEntity;

public interface StatisticService {
  ResponseEntity<?> statisticInfoCourse(Long courseId);
  ResponseEntity<?> statisticBestSeller();
  ResponseEntity<?> generalStatistics();
  ResponseEntity<?> statisticsByDay();
}
