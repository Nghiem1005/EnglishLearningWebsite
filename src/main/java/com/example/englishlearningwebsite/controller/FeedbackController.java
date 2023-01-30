package com.example.englishlearningwebsite.controller;

import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.service.FeedbackService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/feedback")
public class FeedbackController {
  @Autowired
  private FeedbackService feedbackService;

  @GetMapping(value = "")
  public ResponseEntity<?> getAllFeedback(
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return feedbackService.getAllFeedback(pageable);
  }

  @PostMapping(value = "")
  public ResponseEntity<ResponseObject> createFeedback(@RequestParam(name = "studentId") Long studentId,
      @RequestParam(name = "courseId") Long courseId,
      @RequestParam(name = "content") String content) {
    return feedbackService.createFeedback(studentId, courseId, content);
  }

  @PutMapping(value = "")
  public ResponseEntity<ResponseObject> updateFeedback(@RequestParam(name = "content") String content,
      @RequestParam(name = "id") Long id) {
    return feedbackService.updateFeedback(content, id);
  }

  @DeleteMapping(value = "")
  public ResponseEntity<ResponseObject> deleteFeedback(@RequestParam(name = "id") Long id) {
    return feedbackService.deleteFeedback(id);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<ResponseObject> getFeedbackById(@PathVariable(name = "id") Long id) {
    return feedbackService.getFeedbackById(id);
  }

  @GetMapping(value = "/course")
  public ResponseEntity<?> getAllFeedbackByCourse(
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(name = "courseId") Long courseId) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return feedbackService.getAllFeedbackByCourse(pageable, courseId);
  }
}
