package com.example.englishlearningwebsite.service;

import com.example.englishlearningwebsite.dto.response.ResponseObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface StudentCourseService {
  ResponseEntity<ResponseObject> createStudentCourse(Long studentId, Long courseId);
  ResponseEntity<ResponseObject> deleteStudentCourse(Long studentId, Long courseId);
  ResponseEntity<ResponseObject> getCourseByStudent(Pageable pageable, Long studentId);
  ResponseEntity<ResponseObject> getStudentByCourse(Pageable pageable, Long courseId);
  ResponseEntity<ResponseObject> updateProgress(Long studentId, Long courseId, int progress);
}
