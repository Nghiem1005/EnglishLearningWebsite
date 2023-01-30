package com.example.englishlearningwebsite.dto.request;

import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillRequestDTO {
  private String paymentMethod;

  private Date payDate;
}
