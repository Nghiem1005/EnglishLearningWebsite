package com.example.englishlearningwebsite.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreExerciseRequestDTO {
  private Long lessonId;
  private Long examId;
  private Long studentId;
  private double score;
}
