package com.example.englishlearningwebsite.service.implement;

import com.example.englishlearningwebsite.dto.request.ExamDetailRequestDTO;
import com.example.englishlearningwebsite.dto.request.ExamRequestDTO;
import com.example.englishlearningwebsite.dto.response.ExamDetailResponseDTO;
import com.example.englishlearningwebsite.dto.response.ExamResponseDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.dto.response.UserResponseDTO;
import com.example.englishlearningwebsite.entities.Exam;
import com.example.englishlearningwebsite.entities.ExamDetail;
import com.example.englishlearningwebsite.entities.PartExam;
import com.example.englishlearningwebsite.entities.Question;
import com.example.englishlearningwebsite.entities.User;
import com.example.englishlearningwebsite.exception.InvalidValueException;
import com.example.englishlearningwebsite.exception.ResourceNotFoundException;
import com.example.englishlearningwebsite.mapper.ExamDetailMapper;
import com.example.englishlearningwebsite.mapper.ExamMapper;
import com.example.englishlearningwebsite.mapper.UserMapper;
import com.example.englishlearningwebsite.repository.ExamDetailRepository;
import com.example.englishlearningwebsite.repository.ExamRepository;
import com.example.englishlearningwebsite.repository.PartExamRepository;
import com.example.englishlearningwebsite.repository.UserRepository;
import com.example.englishlearningwebsite.service.ExamService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ExamServiceImpl implements ExamService {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private ExamRepository examRepository;
  @Autowired
  private PartExamRepository partExamRepository;
  @Autowired
  private ExamDetailRepository examDetailRepository;

  @Override
  public ResponseEntity<?> createExam(Long userId, ExamRequestDTO examRequestDTO) {
    User creator = userRepository.findById(userId)
        .orElseThrow(
            () -> new ResourceNotFoundException("Could not find user with ID = " + userId));

    Exam exam = new Exam();
    exam.setCreator(creator);
    exam.setTime(examRequestDTO.getTime());
    exam.setName(examRequestDTO.getName());
    Exam examSaved = examRepository.save(exam);
    //Save exam detail
    List<ExamDetail> examDetailList = new ArrayList<>();

    for (int i = 0; i < examRequestDTO.getExamDetailRequestDTOS().length; i++) {
      ExamDetailRequestDTO examDetailRequestDTO = examRequestDTO.getExamDetailRequestDTOS()[i];
      ExamDetail examDetail = new ExamDetail();

      PartExam partExam = partExamRepository.findById(examDetailRequestDTO.getPartExamId())
          .orElseThrow(() -> new ResourceNotFoundException(
              "Could not find part exam with ID = " + examDetailRequestDTO.getPartExamId()));

      examDetail.setPartExam(partExam);
      examDetail.setExam(examSaved);

      //Check serial greater previous serial and consecutive
      examDetail.setSerial(i + 1);

      //examDetailRepository.save(examDetail);
      examDetailList.add(examDetail);
    }

    List<ExamDetail> examDetails = examDetailRepository.saveAll(examDetailList);

    UserResponseDTO creatorResponseDTO = UserMapper.INSTANCE.userToUserResponseDTO(creator);

    ExamResponseDTO examResponseDTO = ExamMapper.INSTANCE.examToExamResponseDTO(examSaved);
    examResponseDTO.setCreator(creatorResponseDTO);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Create exam success!", examResponseDTO));

  }

  @Override
  public ResponseEntity<?> updateExam(Long id, ExamRequestDTO examRequestDTO) {
    Exam exam = examRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find exam with ID = " + id));

    exam.setTime(examRequestDTO.getTime());
    exam.setName(examRequestDTO.getName());
    Exam examSaved = examRepository.save(exam);

    //Add part exam to exam
    if (examRequestDTO.getExamDetailRequestDTOS() != null){
      for (int i=0; i<examRequestDTO.getExamDetailRequestDTOS().length; i++){
        ExamDetail examDetail = new ExamDetail();
        examDetail.setExam(examSaved);
        int finalI = i;
        PartExam partExam = partExamRepository.findById(examRequestDTO.getExamDetailRequestDTOS()[i].getPartExamId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Could not find part exam with ID = " + examRequestDTO.getExamDetailRequestDTOS()[finalI].getPartExamId()));
        examDetail.setPartExam(partExam);
        examDetail.setSerial(examRequestDTO.getExamDetailRequestDTOS()[i].getSerial());

        examDetailRepository.save(examDetail);
      }

    }

    ExamResponseDTO examResponseDTO = ExamMapper.INSTANCE.examToExamResponseDTO(examSaved);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Update exam success!", examResponseDTO));
  }

  @Override
  public ResponseEntity<?> deleteExam(Long id) {
    Exam exam = examRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find exam with ID = " + id));

    //Delete exam detail
    List<ExamDetail> examDetailList = examDetailRepository.findExamDetailsByExamOrderBySerial(exam);
    examDetailRepository.deleteAll(examDetailList);

    examRepository.delete(exam);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Delete exam success!"));
  }

  @Override
  public ResponseEntity<?> getExamById(Long id) {
    return null;
  }

  @Override
  public ResponseEntity<?> getAllExam(Pageable pageable) {
    return null;
  }
}
