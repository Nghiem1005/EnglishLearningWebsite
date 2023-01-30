package com.example.englishlearningwebsite.dto.response;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleResponseDTO {
  private Long id;
  private String link;
  private String title;
  private Date startDate;
  private Date endDate;
  private Long lessonId;
}
