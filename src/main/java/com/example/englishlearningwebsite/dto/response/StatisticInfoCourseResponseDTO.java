package com.example.englishlearningwebsite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticInfoCourseResponseDTO {
  private int videoAmount;
  private int exerciseAmount;
  private int testAmount;
  private int studentAmount;
  private int documentAmount;
}
