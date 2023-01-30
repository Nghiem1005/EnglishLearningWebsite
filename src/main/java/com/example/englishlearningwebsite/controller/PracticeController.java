package com.example.englishlearningwebsite.controller;

import com.example.englishlearningwebsite.dto.request.ScorePracticeRequestDTO;
import com.example.englishlearningwebsite.dto.request.ScoreTestRequestDTO;
import com.example.englishlearningwebsite.service.PracticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/practice")
public class PracticeController {
  @Autowired private PracticeService practiceService;
  @PostMapping(value = "")
  public ResponseEntity<?> createPractice(@RequestParam(name = "examId") Long examId, @RequestParam(name = "studentId") Long studentId) {
    return practiceService.createPractice(examId, studentId );
  }

  @PostMapping(value = "/score")
  public ResponseEntity<?> updateScoreStudentPractice(@RequestBody ScorePracticeRequestDTO scorePracticeRequestDTO) {
    return practiceService.updateScoreStudentPractice(scorePracticeRequestDTO);
  }

  @GetMapping(value = "/score")
  public ResponseEntity<?> getcoreStudentPractice(@RequestParam(name = "examId") Long examId, @RequestParam(name = "studentId") Long studentId) {
    return practiceService.getScorePractice(examId, studentId);
  }
}
