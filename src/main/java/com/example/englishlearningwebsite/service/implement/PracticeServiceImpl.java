package com.example.englishlearningwebsite.service.implement;

import com.example.englishlearningwebsite.dto.request.AnswerRequestDTO;
import com.example.englishlearningwebsite.dto.request.PracticeRequestDTO;
import com.example.englishlearningwebsite.dto.request.QuestionRequestDTO;
import com.example.englishlearningwebsite.dto.request.ScoreExerciseRequestDTO;
import com.example.englishlearningwebsite.dto.request.ScorePracticeRequestDTO;
import com.example.englishlearningwebsite.dto.response.AnswerResponseDTO;
import com.example.englishlearningwebsite.dto.response.ExamResponseDTO;
import com.example.englishlearningwebsite.dto.response.PartExamResponseDTO;
import com.example.englishlearningwebsite.dto.response.QuestionResponseDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.dto.response.ScorePracticeResponseDTO;
import com.example.englishlearningwebsite.dto.response.ScoreTestResponseDTO;
import com.example.englishlearningwebsite.dto.response.UserResponseDTO;
import com.example.englishlearningwebsite.entities.Answer;
import com.example.englishlearningwebsite.entities.Course;
import com.example.englishlearningwebsite.entities.Document;
import com.example.englishlearningwebsite.entities.Exam;
import com.example.englishlearningwebsite.entities.Exercise;
import com.example.englishlearningwebsite.entities.Lesson;
import com.example.englishlearningwebsite.entities.PartExam;
import com.example.englishlearningwebsite.entities.Practice;
import com.example.englishlearningwebsite.entities.Question;
import com.example.englishlearningwebsite.entities.Test;
import com.example.englishlearningwebsite.entities.User;
import com.example.englishlearningwebsite.entities.enums.FileType;
import com.example.englishlearningwebsite.entities.enums.PartExamType;
import com.example.englishlearningwebsite.exception.InvalidValueException;
import com.example.englishlearningwebsite.exception.ResourceAlreadyExistsException;
import com.example.englishlearningwebsite.exception.ResourceNotFoundException;
import com.example.englishlearningwebsite.mapper.AnswersMapper;
import com.example.englishlearningwebsite.mapper.ExamMapper;
import com.example.englishlearningwebsite.mapper.PartExamMapper;
import com.example.englishlearningwebsite.mapper.QuestionMapper;
import com.example.englishlearningwebsite.mapper.ScorePracticeMapper;
import com.example.englishlearningwebsite.mapper.ScoreTestMapper;
import com.example.englishlearningwebsite.mapper.UserMapper;
import com.example.englishlearningwebsite.repository.AnswerRepository;
import com.example.englishlearningwebsite.repository.DocumentRepository;
import com.example.englishlearningwebsite.repository.ExamRepository;
import com.example.englishlearningwebsite.repository.ExerciseRepository;
import com.example.englishlearningwebsite.repository.LessonRepository;
import com.example.englishlearningwebsite.repository.PartExamRepository;
import com.example.englishlearningwebsite.repository.PracticeRepository;
import com.example.englishlearningwebsite.repository.QuestionRepository;
import com.example.englishlearningwebsite.repository.UserRepository;
import com.example.englishlearningwebsite.service.FileStorageService;
import com.example.englishlearningwebsite.service.PracticeService;
import com.example.englishlearningwebsite.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PracticeServiceImpl implements PracticeService {
  @Autowired private PracticeRepository practiceRepository;
  @Autowired private ExamRepository examRepository;
  @Autowired private UserRepository userRepository;

  @Override
  public ResponseEntity<ResponseObject> getExamByStudent(Long studentId) {
    User student = userRepository.findById(studentId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find lesson with ID = " + studentId));

    List<Practice> practiceList = practiceRepository.findPracticesByStudent(student);

    //Convert exam to ExamResponseDTO
    List<ExamResponseDTO> examResponseDTOS = new ArrayList<>();
    for (Practice practice : practiceList){
      examResponseDTOS.add(ExamMapper.INSTANCE.examToExamResponseDTO(practice.getExam()));
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Get exam by student ID = " + studentId, examResponseDTOS));
  }

  @Override
  public ResponseEntity<ResponseObject> createPractice(Long examId, Long studentId) {
    Exam exam = examRepository.findById(examId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find exam with ID = " + examId));

    User student = userRepository.findById(studentId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find student with ID = " + studentId));

    Practice practice = new Practice();
    practice.setExam(exam);
    practice.setStudent(student);

    practiceRepository.save(practice);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create practice success!", student));
  }

  @Override
  public ResponseEntity<ResponseObject> updatePractice(Long id, PracticeRequestDTO practiceRequestDTO) {
    return null;
  }

  @Override
  public ResponseEntity<ResponseObject> updateScoreStudentPractice(
      ScorePracticeRequestDTO scorePracticeRequestDTO) {
    Exam exam = examRepository.findById(scorePracticeRequestDTO.getExamId())
        .orElseThrow(() -> new ResourceNotFoundException("Could not find exam with ID = " + scorePracticeRequestDTO.getExamId()));

    User student = userRepository.findById(scorePracticeRequestDTO.getStudentId())
        .orElseThrow(() -> new ResourceNotFoundException("Could not find student with ID = " + scorePracticeRequestDTO.getStudentId()));

    //Create practice
    Practice practice = new Practice();

    List<Practice> practiceStudentList = practiceRepository.findPracticesByStudentAndExam(student, exam);
    if (practiceStudentList.size() != 0){
      List<Practice> practiceList = practiceRepository.findPracticesByStudentAndExamAndScoresIsNull(student, exam);

      if (practiceList.size() == 0){
        practice.setStudent(student);
        practice.setScores(scorePracticeRequestDTO.getScore());
        practice.setExam(exam);
      } else {
        practice = practiceList.get(0);
        practice.setStudent(student);
        practice.setScores(scorePracticeRequestDTO.getScore());
      }
    } else {
      throw new ResourceNotFoundException("Could not find practice with exam ID = " + scorePracticeRequestDTO.getExamId() + " and student ID = " + scorePracticeRequestDTO.getStudentId());
    }

    ScorePracticeResponseDTO scorePracticeResponseDTO = ScorePracticeMapper.INSTANCE.practiceToScorePracticeResponseDTO(practiceRepository.save(practice));

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Update score test user success!", scorePracticeResponseDTO));
  }


  @Override
  public ResponseEntity<ResponseObject> getPracticeById(Long id) {
    return null;
  }

  @Override
  public ResponseEntity<?> getScorePractice(Long examId, Long studentId) {
    Exam exam = examRepository.findById(examId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find exam with ID = " + examId));

    User student = userRepository.findById(studentId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find student with ID = " + studentId));

    Practice practice = practiceRepository.findPracticeByExamAndStudent(exam, student)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find exercise with exam ID = " + examId + " and student ID = " + studentId));

    ScorePracticeResponseDTO scorePracticeResponseDTO = ScorePracticeMapper.INSTANCE.practiceToScorePracticeResponseDTO(practice);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Update score test user success!", scorePracticeResponseDTO));
  }

}
