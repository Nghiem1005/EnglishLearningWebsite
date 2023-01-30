package com.example.englishlearningwebsite.controller;

import com.example.englishlearningwebsite.dto.request.ScoreExerciseRequestDTO;
import com.example.englishlearningwebsite.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/exercise")
public class ExerciseController {
  @Autowired private ExerciseService exerciseService;
  @PostMapping(value = "")
  public ResponseEntity<?> createExercise(@RequestParam(name = "examId") Long examId, @RequestParam(name = "lessonId") Long lessonId) {
    return exerciseService.createExercise(lessonId, examId);
  }

  @PostMapping(value = "/score")
  public ResponseEntity<?> updateScoreStudentExercise(@RequestBody ScoreExerciseRequestDTO scoreExerciseRequestDTO) {
    return exerciseService.updateScoreStudentExercise(scoreExerciseRequestDTO);
  }

  @GetMapping(value = "/score")
  public ResponseEntity<?> getScoreExercise(@RequestParam(name = "examId") Long examId, @RequestParam(name = "lessonId") Long lessonId,
      @RequestParam(name = "studentId") Long studentId) {
    return exerciseService.getScoreExercise(lessonId, examId, studentId);
  }
}
