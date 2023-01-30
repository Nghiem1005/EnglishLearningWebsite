package com.example.englishlearningwebsite.service;

import com.example.englishlearningwebsite.dto.request.TopicRequestDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface TopicService {

  ResponseEntity<ResponseObject> getAllTopicByLesson(Long lessonId, Pageable pageable);

  ResponseEntity<ResponseObject> createTopic(Long lessonId, TopicRequestDTO topicRequestDTO);

  ResponseEntity<ResponseObject> updateTopic(Long id, TopicRequestDTO topicRequestDTO);

  ResponseEntity<ResponseObject> deleteTopic(Long id);
}
