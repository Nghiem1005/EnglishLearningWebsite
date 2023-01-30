package com.example.englishlearningwebsite.service;

import com.example.englishlearningwebsite.dto.request.ExamRequestDTO;
import com.example.englishlearningwebsite.dto.request.PartExamRequestDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

public interface ExamService {
  ResponseEntity<?> createExam(Long userId, ExamRequestDTO examRequestDTO);
  ResponseEntity<?> updateExam(Long id, ExamRequestDTO examRequestDTO);
  ResponseEntity<?> deleteExam(Long id);
  ResponseEntity<?> getExamById(Long id);
  ResponseEntity<?> getAllExam(Pageable pageable);
}
