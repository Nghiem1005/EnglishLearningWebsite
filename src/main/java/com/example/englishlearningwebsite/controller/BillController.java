package com.example.englishlearningwebsite.controller;

import com.example.englishlearningwebsite.dto.request.BillRequestDTO;
import com.example.englishlearningwebsite.service.BillService;
import com.example.englishlearningwebsite.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/bill")
public class BillController {
  @Autowired private BillService billService;
  @PostMapping(value = "")
  public ResponseEntity<?> createBill(@RequestParam(name = "studentId") Long studentId, @RequestParam(name = "courseId") Long courseId, @RequestBody BillRequestDTO billRequestDTO) {
    return billService.saveBill(studentId, courseId, billRequestDTO);
  }

  @PutMapping(value = "")
  public ResponseEntity<?> updateBill(@RequestParam(name = "billId") Long billId, @RequestBody BillRequestDTO billRequestDTO) {
    return billService.updateBill(billId, billRequestDTO);
  }

  @DeleteMapping(value = "")
  public ResponseEntity<?> deleteBill(@RequestParam(name = "billId") Long billId) {
    return billService.deleteBill(billId);
  }

  @GetMapping(value = "")
  public ResponseEntity<?> getAllBill(@RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return billService.getAllBill(pageable);
  }

  @GetMapping(value = "{id}")
  public ResponseEntity<?> getBillById(@PathVariable(name = "id") Long id) {
    return billService.getBillById(id);
  }

  @GetMapping(value = "/student/{studentId}")
  public ResponseEntity<?> getBillByStudent(@PathVariable(name = "studentId") Long studentId) {
    return billService.getBillByStudent(studentId);
  }

  @GetMapping(value = "/course/{courseId}")
  public ResponseEntity<?> getBillByCourse(@PathVariable(name = "courseId") Long courseId) {
    return billService.getBillByCourse(courseId);
  }
}
