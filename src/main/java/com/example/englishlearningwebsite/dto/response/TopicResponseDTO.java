package com.example.englishlearningwebsite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicResponseDTO {
  private Long id;
  private String title;
  private String content;
  private Long lessonId;
  private String lessonName;
}
