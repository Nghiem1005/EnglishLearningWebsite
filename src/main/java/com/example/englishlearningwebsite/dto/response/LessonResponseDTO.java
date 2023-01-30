package com.example.englishlearningwebsite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonResponseDTO {
  private Long id;
  private String name;
  private String document;
  private Long courseId;
  private String courseName;
  private Long teacherId;
  private String teacherName;
}
