package com.example.englishlearningwebsite.dto.request;

import com.google.api.client.util.DateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalendarRequestDTO {
  private String title;
  private Date startDateTime;
  private Date endDateTime;
}
