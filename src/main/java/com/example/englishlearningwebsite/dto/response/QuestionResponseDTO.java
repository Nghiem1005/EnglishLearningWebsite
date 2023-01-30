package com.example.englishlearningwebsite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponseDTO {
  private Long id;
  private String content;
  private int serial;
  private String document;
  private AnswerResponseDTO[] answerResponseDTOS;
}
