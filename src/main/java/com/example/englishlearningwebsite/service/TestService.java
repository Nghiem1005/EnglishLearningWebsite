package com.example.englishlearningwebsite.service;

import com.example.englishlearningwebsite.dto.request.ScoreExerciseRequestDTO;
import com.example.englishlearningwebsite.dto.request.ScoreTestRequestDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface TestService {

  ResponseEntity<?> createTest(Long courseId, Long examId);

  ResponseEntity<?> getExamByCourse(Long courseId);

  ResponseEntity<ResponseObject> updateScoreStudentTest(
      ScoreTestRequestDTO scoreTestRequestDTO);

  ResponseEntity<?> deleteTest(Long courseId, Long examId);
  ResponseEntity<?> getScoreTest(Long courseId, Long examId, Long studentId);
}
