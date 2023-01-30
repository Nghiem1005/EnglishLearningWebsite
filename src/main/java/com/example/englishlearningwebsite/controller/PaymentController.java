package com.example.englishlearningwebsite.controller;

import com.example.englishlearningwebsite.dto.request.PaymentRequestDTO;
import com.example.englishlearningwebsite.service.PaymentService;
import com.mservice.models.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class PaymentController {
  public static final String SUCCESS_URL = "success";
  public static final String CANCEL_URL = "cancel";

  public static final String RETURN_URL = "courses";
  @Autowired private PaymentService paymentService;

  @PostMapping(value = "/momo")
  public ResponseEntity<PaymentResponse> momo(@RequestBody PaymentRequestDTO paymentRequestDTO) throws Exception{
    return paymentService.createPaymentMomo(paymentRequestDTO,
        "http://localhost:8080/" + SUCCESS_URL);
  }

  @GetMapping(value = "/success")
  public RedirectView successPay(@RequestParam(name = "orderId") Long orderId, @RequestParam(name = "resultCode") Integer resultCode,
                                 @RequestParam(name = "extraData") String extraData) {
    paymentService.saveBill(orderId, resultCode, extraData);
    return new RedirectView("http://localhost:3002/" + RETURN_URL);
  }
}
