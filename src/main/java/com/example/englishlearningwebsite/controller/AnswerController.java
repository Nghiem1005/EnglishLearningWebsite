package com.example.englishlearningwebsite.controller;

import com.example.englishlearningwebsite.dto.request.AnswerRequestDTO;
import com.example.englishlearningwebsite.dto.request.QuestionRequestDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.service.AnswerService;
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
@RequestMapping(value = "/api/v1/answer")
public class AnswerController {
  @Autowired private AnswerService answerService;
  @PostMapping(value = "")
  public ResponseEntity<ResponseObject> createAnswer(@RequestParam(name = "questionId") Long questionId,
      @RequestBody AnswerRequestDTO answerRequestDTO) {
    return answerService.createAnswer(questionId, answerRequestDTO);
  }

  @PutMapping(value = "")
  public ResponseEntity<ResponseObject> updateAnswer(@RequestBody AnswerRequestDTO answerRequestDTO,
      @RequestParam(name = "id") Long id) {
    return answerService.updateAnswer(id, answerRequestDTO);
  }

  @DeleteMapping(value = "")
  public ResponseEntity<ResponseObject> deleteAnswer(@RequestParam(name = "id") Long id) {
    return answerService.deleteAnswer(id);
  }

  @GetMapping(value = "/question")
  public ResponseEntity<?> getAllAnswerByQuestion(
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(name = "questionId") Long questionId) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return answerService.getAllAnswerByQuestion(pageable, questionId);
  }
}
