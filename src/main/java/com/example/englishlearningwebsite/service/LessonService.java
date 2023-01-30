package com.example.englishlearningwebsite.service;

import com.example.englishlearningwebsite.dto.request.LessonRequestDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.services.calendar.Calendar;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface LessonService {
  ResponseEntity<ResponseObject> getAllLessonByCourse(Pageable pageable, Long courseId);

  ResponseEntity<ResponseObject> createLesson(Long courseId, LessonRequestDTO lessonRequestDTO);

  ResponseEntity<ResponseObject> updateLesson(Long id, LessonRequestDTO lessonRequestDTO);

  ResponseEntity<ResponseObject> deleteLesson(Long id, String code, GoogleAuthorizationCodeFlow flow, Calendar client)
      throws GeneralSecurityException, IOException;

  ResponseEntity<ResponseObject> getLessonById(Long id);
}
