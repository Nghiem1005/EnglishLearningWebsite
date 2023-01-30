package com.example.englishlearningwebsite.service;

import com.example.englishlearningwebsite.entities.Exam;
import org.springframework.http.ResponseEntity;

public interface ExamDetailService {
  ResponseEntity<?> getPartExamByExam(Long examId);
  ResponseEntity<?> addPartExamToExam(Long examId, Long partExamId);
  ResponseEntity<?> deletePartExamToExam(Long examId, Long partExamId);
}
