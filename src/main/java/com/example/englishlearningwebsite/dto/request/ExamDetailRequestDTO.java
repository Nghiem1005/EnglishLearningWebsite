package com.example.englishlearningwebsite.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamDetailRequestDTO {
  private Long partExamId;
  private int serial;
}
