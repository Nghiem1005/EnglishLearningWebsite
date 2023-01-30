package com.example.englishlearningwebsite.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerRequestDTO {
  private String content;
  private boolean correct;
}
