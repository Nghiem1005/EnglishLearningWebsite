package com.example.englishlearningwebsite.controller;

import com.example.englishlearningwebsite.dto.request.TopicRequestDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.service.TopicService;
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
@RequestMapping(value = "/api/v1/topic")
public class TopicController {
  @Autowired private TopicService topicService;
  @PostMapping(value = "")
  public ResponseEntity<ResponseObject> createTopic(@RequestParam(name = "lessonId") Long lessonId,
      @RequestBody TopicRequestDTO topicRequestDTO) {
    return topicService.createTopic(lessonId, topicRequestDTO);
  }

  @PutMapping(value = "")
  public ResponseEntity<ResponseObject> updateTopic(@RequestBody TopicRequestDTO topicRequestDTO,
      @RequestParam(name = "id") Long id) {
    return topicService.updateTopic(id, topicRequestDTO);
  }

  @DeleteMapping(value = "")
  public ResponseEntity<ResponseObject> deleteTopic(@RequestParam(name = "id") Long id) {
    return topicService.deleteTopic(id);
  }

  @GetMapping(value = "/lesson")
  public ResponseEntity<?> getAllTopicByLesson(
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(name = "lessonId") Long lessonId) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return topicService.getAllTopicByLesson(lessonId, pageable);
  }
}
