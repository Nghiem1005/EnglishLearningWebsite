package com.example.englishlearningwebsite.service;

import com.example.englishlearningwebsite.dto.request.PracticeRequestDTO;
import com.example.englishlearningwebsite.dto.request.ScoreExerciseRequestDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface ExerciseService {
  ResponseEntity<?> createExercise(Long lessonId, Long examId);
  ResponseEntity<?> getExamByLesson(Long lessonId);
  ResponseEntity<ResponseObject> updateScoreStudentExercise(ScoreExerciseRequestDTO scoreExerciseRequestDTO);
  ResponseEntity<?> deleteExercise(Long lessonId, Long examId);
  ResponseEntity<?> getScoreExercise(Long lessonId, Long examId, Long studentId);
}
