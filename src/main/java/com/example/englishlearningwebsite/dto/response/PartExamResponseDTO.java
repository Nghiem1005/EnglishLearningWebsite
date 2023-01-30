package com.example.englishlearningwebsite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartExamResponseDTO {
  private Long id;
  private String name;
  private String documents;
  private String type;
  private UserResponseDTO creator;
  private QuestionResponseDTO[] questionResponseDTOS;
}
