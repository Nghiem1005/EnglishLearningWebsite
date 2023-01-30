package com.example.englishlearningwebsite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticByDayResponseDTO {
  private String time;
  private int studentCourseWithVideo;
  private int studentCourseWithTeacher;
  private int newMember;
  private double revenueCourseVideo;
  private double revenueCourseTeacher;
  private double revenueTotal;
}
