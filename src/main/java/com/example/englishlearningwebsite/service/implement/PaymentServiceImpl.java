package com.example.englishlearningwebsite.service.implement;

import com.example.englishlearningwebsite.dto.request.PaymentRequestDTO;
import com.example.englishlearningwebsite.dto.response.BillResponseDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.entities.Bill;
import com.example.englishlearningwebsite.entities.Course;
import com.example.englishlearningwebsite.entities.StudentCourse;
import com.example.englishlearningwebsite.entities.User;
import com.example.englishlearningwebsite.exception.ResourceNotFoundException;
import com.example.englishlearningwebsite.mapper.BillMapper;
import com.example.englishlearningwebsite.repository.BillRepository;
import com.example.englishlearningwebsite.repository.CourseRepository;
import com.example.englishlearningwebsite.repository.StudentCourseRepository;
import com.example.englishlearningwebsite.repository.UserRepository;
import com.example.englishlearningwebsite.service.PaymentService;
import com.mservice.config.Environment;
import com.mservice.enums.RequestType;
import com.mservice.models.PaymentResponse;
import com.mservice.processor.CreateOrderMoMo;
import com.mservice.shared.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PaymentServiceImpl implements PaymentService {
  @Autowired private BillRepository billRepository;
  @Autowired private StudentCourseRepository studentCourseRepository;
  @Autowired private CourseRepository courseRepository;
  @Autowired private UserRepository userRepository;

  @Override
  public ResponseEntity<PaymentResponse> createPaymentMomo(PaymentRequestDTO paymentRequestDTO, String returnUrl) throws Exception{
    LogUtils.init();
    Environment environment = Environment.selectEnv("dev");
    String requestId = String.valueOf(System.currentTimeMillis());
    //Create bill momo
    PaymentResponse responseObject = CreateOrderMoMo.process(environment, String.valueOf(System.currentTimeMillis()), requestId, paymentRequestDTO.getPrice().toString(), paymentRequestDTO.getDescription(), returnUrl, returnUrl, paymentRequestDTO.getCourseId().toString() + "," + paymentRequestDTO.getStudentId().toString(), RequestType.CAPTURE_WALLET, true);
    return ResponseEntity.status(HttpStatus.OK).body(responseObject);
  }

  @Override
  public ResponseEntity<?> saveBill(Long billId, Integer resultCode, String extraData) {
    String message = "Payment success!";
    //Bill bill = billRepository.findById(billId).orElseThrow(() -> new ResourceNotFoundException("Could not find bill with ID = " + billId));
    HttpStatus httpStatus = HttpStatus.OK;
    if (resultCode == null || resultCode != 0){
      //billRepository.delete(bill);
      message = "Payment fail!";
      httpStatus = HttpStatus.BAD_REQUEST;
    } else {
      String[] idList = extraData.split(",");
      //Create bill
      Course course = courseRepository.findById(Long.parseLong(idList[0]))
              .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + Long.parseLong(idList[0])));

      User student = userRepository.findById(Long.parseLong(idList[1]))
              .orElseThrow(() -> new ResourceNotFoundException("Could not find student with ID = " + Long.parseLong(idList[1])));

      Bill bill = new Bill();
      bill.setPaymentMethod("MOMO");
      bill.setPayDate(new Date());
      bill.setTotalPrice(course.getPrice());
      bill.setCourse(course);
      bill.setUser(student);

      BillResponseDTO billResponseDTO = BillMapper.INSTANCE.billToBillResponseDTO(billRepository.save(bill));
      //Attend student in course
      StudentCourse studentCourse = new StudentCourse();
      studentCourse.setCourse(bill.getCourse());
      studentCourse.setUser(bill.getUser());
      studentCourse.setProgress(1);
      studentCourseRepository.save(studentCourse);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(httpStatus, message));
  }
}
