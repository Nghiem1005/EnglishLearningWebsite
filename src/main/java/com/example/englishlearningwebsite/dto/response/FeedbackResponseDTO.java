package com.example.englishlearningwebsite.dto.response;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponseDTO {
  private Long id;
  private String content;
  private Long studentId;
  private String studentName;
  private Long courseId;
  private String courseName;
  private Date createDate;
  private Date updateDate;
}