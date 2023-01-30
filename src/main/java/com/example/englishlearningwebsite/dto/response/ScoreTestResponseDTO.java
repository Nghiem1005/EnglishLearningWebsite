package com.example.englishlearningwebsite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreTestResponseDTO {
  private Long courseId;
  private Long examId;
  private Long studentId;
  private double score;
}
