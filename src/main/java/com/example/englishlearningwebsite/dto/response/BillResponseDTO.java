package com.example.englishlearningwebsite.dto.response;

import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillResponseDTO {
  private Long billId;

  private BigDecimal totalPrice;

  private String paymentMethod;

  private Date payDate;

  private Long courseId;

  private Long studentId;

  private String studentName;
}
