package com.example.englishlearningwebsite.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequestDTO {
  private String content;
  private MultipartFile document;
  private AnswerRequestDTO[] answerRequestDTOS;
}
