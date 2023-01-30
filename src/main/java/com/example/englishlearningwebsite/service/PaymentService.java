package com.example.englishlearningwebsite.service;

import com.example.englishlearningwebsite.dto.request.PaymentRequestDTO;
import com.mservice.models.PaymentResponse;
import org.springframework.http.ResponseEntity;

public interface PaymentService {
  ResponseEntity<PaymentResponse> createPaymentMomo(PaymentRequestDTO paymentRequestDTO, String returnUrl) throws Exception;
  ResponseEntity<?> saveBill(Long billId, Integer resultCode, String extraData);
}
