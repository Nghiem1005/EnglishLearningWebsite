package com.example.englishlearningwebsite.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartExamRequestDTO {
  private String name;
  private QuestionRequestDTO[] questionRequestDTOS;
  private String type;
}
