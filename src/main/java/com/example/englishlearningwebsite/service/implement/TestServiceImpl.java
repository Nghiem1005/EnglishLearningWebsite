package com.example.englishlearningwebsite.service.implement;

import com.example.englishlearningwebsite.dto.request.ScoreTestRequestDTO;
import com.example.englishlearningwebsite.dto.response.ExamResponseDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.dto.response.ScoreExerciseResponseDTO;
import com.example.englishlearningwebsite.dto.response.ScoreTestResponseDTO;
import com.example.englishlearningwebsite.entities.Course;
import com.example.englishlearningwebsite.entities.Exam;
import com.example.englishlearningwebsite.entities.Exercise;
import com.example.englishlearningwebsite.entities.Lesson;
import com.example.englishlearningwebsite.entities.Test;
import com.example.englishlearningwebsite.entities.User;
import com.example.englishlearningwebsite.exception.ResourceNotFoundException;
import com.example.englishlearningwebsite.mapper.ExamMapper;
import com.example.englishlearningwebsite.mapper.ScoreExerciseMapper;
import com.example.englishlearningwebsite.mapper.ScoreTestMapper;
import com.example.englishlearningwebsite.repository.CourseRepository;
import com.example.englishlearningwebsite.repository.ExamRepository;
import com.example.englishlearningwebsite.repository.TestRepository;
import com.example.englishlearningwebsite.repository.UserRepository;
import com.example.englishlearningwebsite.service.TestService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
  @Autowired private ExamRepository examRepository;
  @Autowired private CourseRepository courseRepository;
  @Autowired private TestRepository testRepository;
  @Autowired private UserRepository userRepository;

  @Override
  public ResponseEntity<?> createTest(Long courseId, Long examId) {
    Exam exam = examRepository.findById(examId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find exam with ID = " + examId));

    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

    Test test = new Test();
    test.setExam(exam);
    test.setCourse(course);

    testRepository.save(test);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create test success!", test));
  }

  @Override
  public ResponseEntity<?> getExamByCourse(Long courseId) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

    List<Test> testList = testRepository.findTestsByCourse(course);

    List<ExamResponseDTO> examResponseDTOS = new ArrayList<>();
    for (Test test : testList){
      examResponseDTOS.add(ExamMapper.INSTANCE.examToExamResponseDTO(test.getExam()));
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Get exam by course ID = " + courseId, examResponseDTOS));
  }

  @Override
  public ResponseEntity<ResponseObject> updateScoreStudentTest(
      ScoreTestRequestDTO scoreTestRequestDTO) {
    Course course = courseRepository.findById(scoreTestRequestDTO.getCourseId())
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + scoreTestRequestDTO.getCourseId()));

    Exam exam = examRepository.findById(scoreTestRequestDTO.getExamId())
        .orElseThrow(() -> new ResourceNotFoundException("Could not find exam with ID = " + scoreTestRequestDTO.getExamId()));

    User student = userRepository.findById(scoreTestRequestDTO.getStudentId())
        .orElseThrow(() -> new ResourceNotFoundException("Could not find student with ID = " + scoreTestRequestDTO.getStudentId()));

    //Create test by exam
    Test test = new Test();

    List<Test> testStudentList = testRepository.findTestsByCourseAndExam(course, exam);
    if (testStudentList.size() != 0){
      List<Test> testList = testRepository.findTestsByCourseAndExamAndStudentIsNull(course, exam);

      if (testList.size() == 0){
        test.setStudent(student);
        test.setScores(scoreTestRequestDTO.getScore());
        test.setCourse(course);
        test.setExam(exam);
      } else {
        test = testList.get(0);
        test.setStudent(student);
        test.setScores(scoreTestRequestDTO.getScore());
      }
    } else {
      throw new ResourceNotFoundException("Could not find test with exam ID = " + scoreTestRequestDTO.getExamId() + " and course ID = " + scoreTestRequestDTO.getCourseId());
    }

    ScoreTestResponseDTO scoreTestResponseDTO = ScoreTestMapper.INSTANCE.testToScoreTestResponseDTO(testRepository.save(test));

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Update score test user success!", scoreTestResponseDTO));
  }

  @Override
  public ResponseEntity<?> deleteTest(Long courseId, Long examId) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

    Exam exam = examRepository.findById(examId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find exam with ID = " + examId));

    List<Test> testList = testRepository.findTestsByCourseAndExam(course, exam);

    testRepository.deleteAll(testList);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Delete test success!"));
  }

  @Override
  public ResponseEntity<?> getScoreTest(Long courseId, Long examId, Long studentId) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

    Exam exam = examRepository.findById(examId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find exam with ID = " + examId));

    User student = userRepository.findById(studentId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find student with ID = " + studentId));

    Test test = testRepository.findTestByCourseAndExamAndStudent(course, exam, student)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find test with student ID = " + studentId + " and exam ID = " + examId +
            " and course ID = " + courseId));

    ScoreTestResponseDTO scoreTestResponseDTO = ScoreTestMapper.INSTANCE.testToScoreTestResponseDTO(test);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Get score test success!", scoreTestResponseDTO));
  }
}
