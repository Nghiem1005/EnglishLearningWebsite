package com.example.englishlearningwebsite.service;

import com.example.englishlearningwebsite.dto.request.PracticeRequestDTO;
import com.example.englishlearningwebsite.dto.request.ScoreExerciseRequestDTO;
import com.example.englishlearningwebsite.dto.request.ScorePracticeRequestDTO;
import com.example.englishlearningwebsite.dto.request.TopicRequestDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface PracticeService{
  ResponseEntity<ResponseObject> getExamByStudent(Long studentId);

  ResponseEntity<ResponseObject> createPractice(Long examId, Long studentId);

  ResponseEntity<ResponseObject> updatePractice(Long id, PracticeRequestDTO practiceRequestDTO);
  ResponseEntity<ResponseObject> updateScoreStudentPractice(
      ScorePracticeRequestDTO scorePracticeRequestDTO);

  ResponseEntity<ResponseObject> getPracticeById(Long id);
  ResponseEntity<?> getScorePractice(Long examId, Long studentId);
}
