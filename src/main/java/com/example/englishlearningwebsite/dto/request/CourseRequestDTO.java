package com.example.englishlearningwebsite.dto.request;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequestDTO {
  @NotNull(message = "Course name is required")
  private String name;

  private MultipartFile[] documents;

  private BigDecimal price;

  private String description;

  private MultipartFile thumbnail;

  private String level;

  private String courseType;
}
