package com.example.englishlearningwebsite.controller;

import com.example.englishlearningwebsite.dto.request.LessonRequestDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.service.LessonService;
import com.example.englishlearningwebsite.utils.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/lesson")
public class LessonController {
  @Autowired private LessonService lessonService;

  @PostMapping(value = "")
  public ResponseEntity<ResponseObject> createLesson(@RequestParam(name = "courseId") Long courseId,
      @ModelAttribute LessonRequestDTO lessonRequestDTO) {
    return lessonService.createLesson(courseId, lessonRequestDTO);
  }

  @PutMapping(value = "")
  public ResponseEntity<ResponseObject> updateLesson(@ModelAttribute LessonRequestDTO lessonRequestDTO,
      @RequestParam(name = "id") Long id) {
    return lessonService.updateLesson(id, lessonRequestDTO);
  }


  @GetMapping(value = "/{id}")
  public ResponseEntity<ResponseObject> getLessonById(@PathVariable(name = "id") Long id) {
    return lessonService.getLessonById(id);
  }

  @GetMapping(value = "/course")
  public ResponseEntity<?> getAllLessonByCourse(
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(name = "courseId") Long courseId) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return lessonService.getAllLessonByCourse(pageable, courseId);
  }
}
