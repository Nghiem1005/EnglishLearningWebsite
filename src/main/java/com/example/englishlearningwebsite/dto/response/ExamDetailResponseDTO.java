package com.example.englishlearningwebsite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamDetailResponseDTO {
  private Long examId;
  private int serial;
  private PartExamResponseDTO partExamResponseDTO;
}
