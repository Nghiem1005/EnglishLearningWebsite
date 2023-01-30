package com.example.englishlearningwebsite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreExerciseResponseDTO {
  private Long lessonId;
  private Long examId;
  private Long studentId;
  private double score;
}
