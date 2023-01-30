package com.example.englishlearningwebsite.service;

import com.example.englishlearningwebsite.dto.request.CourseRequestDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.services.calendar.Calendar;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface CourseService {
  ResponseEntity<ResponseObject> saveCourse(Long teacherId, CourseRequestDTO courseRequestDTO);
  ResponseEntity<ResponseObject> updateCourse(Long courseId, CourseRequestDTO courseRequestDTO);
  ResponseEntity<ResponseObject> deleteCourse(Long courseId, String code, GoogleAuthorizationCodeFlow flow, Calendar client)
      throws GeneralSecurityException, IOException;
  ResponseEntity<?> getAllCourse(Pageable pageable);
  ResponseEntity<ResponseObject> getCourseById(Long id);
  ResponseEntity<ResponseObject> getCourseByTeacher(Pageable pageable, Long teacherId);
  ResponseEntity<ResponseObject> getCourseByNotTeacher(Pageable pageable);
  ResponseEntity<?> getCourseByType(Pageable pageable, String type);
}
