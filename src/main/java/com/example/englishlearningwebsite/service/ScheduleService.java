package com.example.englishlearningwebsite.service;

import com.example.englishlearningwebsite.dto.request.CalendarRequestDTO;
import com.example.englishlearningwebsite.dto.request.RoleRequestDTO;
import com.example.englishlearningwebsite.dto.request.TopicRequestDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.services.calendar.Calendar;
import java.io.IOException;
import java.security.GeneralSecurityException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ScheduleService {
  ResponseEntity<ResponseObject> createSchedule(Long lessonId, String code, CalendarRequestDTO calendarRequestDTO, GoogleAuthorizationCodeFlow flow, GoogleClientSecrets clientSecrets, Calendar client)
      throws IOException, GeneralSecurityException;

  ResponseEntity<ResponseObject> updateSchedule(CalendarRequestDTO calendarRequestDTO, Long id, String code, GoogleAuthorizationCodeFlow flow, GoogleClientSecrets clientSecrets, Calendar client)
      throws IOException, GeneralSecurityException;

  ResponseEntity<ResponseObject> deleteSchedule(Long id, String code, GoogleAuthorizationCodeFlow flow, GoogleClientSecrets clientSecrets, Calendar client)
      throws IOException, GeneralSecurityException;

  ResponseEntity<ResponseObject> getScheduleByCourse(Long courseId);

  ResponseEntity<?> getScheduleByLesson(Long lessonId);

  ResponseEntity<?> getScheduleByTeacher(Long teacherId);

  ResponseEntity<?> getScheduleById(Long id);
}
