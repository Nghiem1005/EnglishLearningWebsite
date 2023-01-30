package com.example.englishlearningwebsite.controller;

import com.example.englishlearningwebsite.dto.request.QuestionRequestDTO;
import com.example.englishlearningwebsite.dto.request.TopicRequestDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.service.QuestionService;
import com.example.englishlearningwebsite.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/question")
public class QuestionController {
  @Autowired private QuestionService questionService;
  @PostMapping(value = "")
  public ResponseEntity<ResponseObject> createQuestion(@RequestParam(name = "practiceId") Long practiceId,
      @RequestBody QuestionRequestDTO questionRequestDTO) {
    return questionService.createQuestion(practiceId, questionRequestDTO);
  }

  @PutMapping(value = "")
  public ResponseEntity<ResponseObject> updateQuestion(@RequestBody QuestionRequestDTO questionRequestDTO,
      @RequestParam(name = "id") Long id) {
    return questionService.updateQuestion(id, questionRequestDTO);
  }

  @DeleteMapping(value = "")
  public ResponseEntity<ResponseObject> deleteQuestion(@RequestParam(name = "id") Long id) {
    return questionService.deleteQuestion(id);
  }

  @GetMapping(value = "/practice")
  public ResponseEntity<?> getAllQuestionByPractice(
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(name = "practiceId") Long practiceId) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return questionService.getAllQuestionByPractice(pageable, practiceId);
  }
}
