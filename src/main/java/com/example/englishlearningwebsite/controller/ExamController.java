package com.example.englishlearningwebsite.controller;

import com.example.englishlearningwebsite.dto.request.ExamRequestDTO;
import com.example.englishlearningwebsite.dto.request.PartExamRequestDTO;
import com.example.englishlearningwebsite.service.ExamService;
import com.example.englishlearningwebsite.service.ExerciseService;
import com.example.englishlearningwebsite.service.PartExamService;
import com.example.englishlearningwebsite.service.PracticeService;
import com.example.englishlearningwebsite.service.TestService;
import com.example.englishlearningwebsite.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/exam")
public class ExamController {
  @Autowired
  private ExamService examService;
  @Autowired private ExerciseService exerciseService;
  @Autowired private PracticeService practiceService;
  @Autowired private TestService testService;
  @PostMapping(value = "")
  public ResponseEntity<?> createExam(@RequestParam(name = "creatorId") Long creatorId, @RequestBody ExamRequestDTO examRequestDTO) {
    return examService.createExam(creatorId,examRequestDTO );
  }

  @PutMapping(value = "")
  public ResponseEntity<?> updateExam(@RequestParam(name = "id") Long id, @RequestBody ExamRequestDTO examRequestDTO) {
    return examService.updateExam(id, examRequestDTO);
  }

  @DeleteMapping(value = "")
  public ResponseEntity<?> deleteExam(@RequestParam(name = "id") Long id) {
    return examService.deleteExam(id);
  }

  @GetMapping(value = "")
  public ResponseEntity<?> getAllExam(@RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page){
    Pageable pageable = PageRequest.of(page - 1, size);
    return examService.getAllExam(pageable);
  }

  @GetMapping(value = "/lesson")
  public ResponseEntity<?> getExamByLesson(@RequestParam(name = "lessonId") Long lessonId){
    return exerciseService.getExamByLesson(lessonId);
  }

  @GetMapping(value = "/student")
  public ResponseEntity<?> getExamByStudent(@RequestParam(name = "practiceId") Long practiceId){
    return practiceService.getExamByStudent(practiceId);
  }

  @GetMapping(value = "/course")
  public ResponseEntity<?> getExamByCourse(@RequestParam(name = "courseId") Long courseId){
    return testService.getExamByCourse(courseId);
  }
}
