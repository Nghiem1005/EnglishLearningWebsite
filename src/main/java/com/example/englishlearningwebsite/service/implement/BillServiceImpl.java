package com.example.englishlearningwebsite.service.implement;

import com.example.englishlearningwebsite.dto.request.BillRequestDTO;
import com.example.englishlearningwebsite.dto.response.BillResponseDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.entities.Bill;
import com.example.englishlearningwebsite.entities.Course;
import com.example.englishlearningwebsite.entities.User;
import com.example.englishlearningwebsite.exception.ResourceNotFoundException;
import com.example.englishlearningwebsite.mapper.BillMapper;
import com.example.englishlearningwebsite.repository.BillRepository;
import com.example.englishlearningwebsite.repository.CourseRepository;
import com.example.englishlearningwebsite.repository.UserRepository;
import com.example.englishlearningwebsite.service.BillService;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BillServiceImpl implements BillService {
  @Autowired private BillRepository billRepository;
  @Autowired private CourseRepository courseRepository;
  @Autowired private UserRepository userRepository;

  @Override
  public ResponseEntity<ResponseObject> saveBill(Long studentId, Long courseId,
      BillRequestDTO billRequestDTO) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

    User student = userRepository.findById(studentId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find student with ID = " + studentId));

    Bill bill = new Bill();
    bill.setPaymentMethod(billRequestDTO.getPaymentMethod());
    bill.setPayDate(billRequestDTO.getPayDate());
    bill.setTotalPrice(course.getPrice());
    bill.setCourse(course);
    bill.setUser(student);

    BillResponseDTO billResponseDTO = BillMapper.INSTANCE.billToBillResponseDTO(billRepository.save(bill));
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create bill success!", billResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> updateBill(Long billId, BillRequestDTO billRequestDTO) {
    Bill bill = billRepository.findById(billId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find bill with ID = " + billId));

    bill.setPaymentMethod(billRequestDTO.getPaymentMethod());
    bill.setPayDate(billRequestDTO.getPayDate());

    BillResponseDTO billResponseDTO = BillMapper.INSTANCE.billToBillResponseDTO(billRepository.save(bill));
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create bill success!", billResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> deleteBill(Long billId) {
    Bill bill = billRepository.findById(billId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find bill with ID = " + billId));

    billRepository.delete(bill);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Delete bill success!"));
  }

  @Override
  public ResponseEntity<ResponseObject> getAllBill(Pageable pageable) {
    Page<Bill> getBillList = billRepository.findAll(pageable);
    List<Bill> billList = getBillList.getContent();
    List<BillResponseDTO> billResponseDTOList = new ArrayList<>();
    for (Bill bill : billList){
      BillResponseDTO billResponseDTO = BillMapper.INSTANCE.billToBillResponseDTO(bill);
      billResponseDTOList.add(billResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List bill", billResponseDTOList, getBillList.getTotalPages()));
  }

  @Override
  public ResponseEntity<ResponseObject> getBillById(Long billId) {
    Bill bill = billRepository.findById(billId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find bill with ID = " + billId));
    BillResponseDTO billResponseDTO = BillMapper.INSTANCE.billToBillResponseDTO(bill);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List bill", billResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> getBillByStudent(Long studentId) {
    User student = userRepository.findById(studentId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find student with ID = " + studentId));

    List<Bill> billList = billRepository.findBillsByUser(student);
    List<BillResponseDTO> billResponseDTOList = new ArrayList<>();
    for (Bill bill : billList){
      BillResponseDTO billResponseDTO = BillMapper.INSTANCE.billToBillResponseDTO(bill);
      billResponseDTOList.add(billResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List bill", billResponseDTOList));
  }

  @Override
  public ResponseEntity<ResponseObject> getBillByCourse(Long courseId) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

    List<Bill> billList = billRepository.findBillsByCourse(course);
    List<BillResponseDTO> billResponseDTOList = new ArrayList<>();
    for (Bill bill : billList){
      BillResponseDTO billResponseDTO = BillMapper.INSTANCE.billToBillResponseDTO(bill);
      billResponseDTOList.add(billResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List bill", billResponseDTOList));
  }
}
