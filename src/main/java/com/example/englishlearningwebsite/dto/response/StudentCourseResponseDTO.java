package com.example.englishlearningwebsite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCourseResponseDTO {
  private Long studentId;
  private String studentName;
  private Long courseId;
  private String courseName;
  private Long teacherId;
  private String teacherName;
  private int progress;
}
