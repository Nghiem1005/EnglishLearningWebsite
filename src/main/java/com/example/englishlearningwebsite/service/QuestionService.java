package com.example.englishlearningwebsite.service;

import com.example.englishlearningwebsite.dto.request.LessonRequestDTO;
import com.example.englishlearningwebsite.dto.request.QuestionRequestDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface QuestionService {
  ResponseEntity<ResponseObject> getAllQuestionByPractice(Pageable pageable, Long practiceId);

  ResponseEntity<ResponseObject> createQuestion(Long questionId, QuestionRequestDTO questionRequestDTO);

  ResponseEntity<ResponseObject> updateQuestion(Long id, QuestionRequestDTO questionRequestDTO);

  ResponseEntity<ResponseObject> deleteQuestion(Long id);
}
