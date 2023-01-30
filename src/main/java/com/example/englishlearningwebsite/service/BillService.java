package com.example.englishlearningwebsite.service;

import com.example.englishlearningwebsite.dto.request.BillRequestDTO;
import com.example.englishlearningwebsite.dto.request.CourseRequestDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface BillService {
  ResponseEntity<ResponseObject> saveBill(Long studentId, Long courseId, BillRequestDTO billRequestDTO);
  ResponseEntity<ResponseObject> updateBill(Long billId, BillRequestDTO billRequestDTO);
  ResponseEntity<ResponseObject> deleteBill(Long billId);
  ResponseEntity<ResponseObject> getAllBill(Pageable pageable);
  ResponseEntity<ResponseObject> getBillById(Long billId);
  ResponseEntity<ResponseObject> getBillByStudent(Long studentId);
  ResponseEntity<ResponseObject> getBillByCourse(Long courseId);
}
