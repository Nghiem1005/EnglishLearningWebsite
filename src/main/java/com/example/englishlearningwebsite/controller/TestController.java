package com.example.englishlearningwebsite.controller;

import com.example.englishlearningwebsite.dto.request.ScoreExerciseRequestDTO;
import com.example.englishlearningwebsite.dto.request.ScoreTestRequestDTO;
import com.example.englishlearningwebsite.service.ExerciseService;
import com.example.englishlearningwebsite.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/test")
public class TestController {
  @Autowired
  private TestService testService;
  @PostMapping(value = "")
  public ResponseEntity<?> createTest(@RequestParam(name = "examId") Long examId, @RequestParam(name = "courseId") Long courseId) {
    return testService.createTest(courseId,examId );
  }

  @PostMapping(value = "/score")
  public ResponseEntity<?> updateScoreStudentTest(@RequestBody ScoreTestRequestDTO scoreTestRequestDTO) {
    return testService.updateScoreStudentTest(scoreTestRequestDTO);
  }

  @GetMapping(value = "/score")
  public ResponseEntity<?> getScoreTest(@RequestParam(name = "examId") Long examId, @RequestParam(name = "studentId") Long studentId,
      @RequestParam(name = "courseId") Long courseId) {
    return testService.getScoreTest(courseId, examId, studentId);
  }
}
