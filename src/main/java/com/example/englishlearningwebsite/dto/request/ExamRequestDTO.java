package com.example.englishlearningwebsite.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamRequestDTO {
  private String name;
  private int time;
  private ExamDetailRequestDTO[] examDetailRequestDTOS;
}
