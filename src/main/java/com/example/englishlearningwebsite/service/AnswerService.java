package com.example.englishlearningwebsite.service;

import com.example.englishlearningwebsite.dto.request.AnswerRequestDTO;
import com.example.englishlearningwebsite.dto.request.QuestionRequestDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface AnswerService {
  ResponseEntity<ResponseObject> getAllAnswerByQuestion(Pageable pageable, Long questionId);

  ResponseEntity<ResponseObject> createAnswer(Long questionId, AnswerRequestDTO answerRequestDTO);

  ResponseEntity<ResponseObject> updateAnswer(Long id, AnswerRequestDTO answerRequestDTO);

  ResponseEntity<ResponseObject> deleteAnswer(Long id);
}
