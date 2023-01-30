package com.example.englishlearningwebsite.service;

import com.example.englishlearningwebsite.dto.response.ResponseObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface FeedbackService {
    ResponseEntity<ResponseObject> getAllFeedback(Pageable pageable);

    ResponseEntity<ResponseObject> getAllFeedbackByCourse(Pageable pageable, Long courseId);

    ResponseEntity<ResponseObject> createFeedback(Long studentId, Long courseId, String content);

    ResponseEntity<ResponseObject> updateFeedback(String content, Long id);

    ResponseEntity<ResponseObject> deleteFeedback(Long id);

    ResponseEntity<ResponseObject> getFeedbackById(Long id);

}