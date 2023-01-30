package com.example.englishlearningwebsite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamResponseDTO {
  private Long id;
  private String name;
  private int time;
  private UserResponseDTO creator;
}
