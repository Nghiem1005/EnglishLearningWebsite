package com.example.englishlearningwebsite.controller;

import com.example.englishlearningwebsite.service.FileStorageService;
import com.example.englishlearningwebsite.service.ImageStorageService;
import com.example.englishlearningwebsite.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FileController {
  @Autowired
  private FileStorageService fileStorageService;
  @Autowired
  private ImageStorageService imageStorageService;

  @GetMapping(value = "/document/download/course/{fileName}")
  public ResponseEntity readFileContent(@PathVariable("fileName") String fileName) {
    Resource resource = fileStorageService.readFileContent(fileName, Utils.FILE_UPLOAD_COURSE_URI);
    return ResponseEntity.status(HttpStatus.OK)
        .contentType(MediaType.parseMediaType("application/octet-stream"))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + fileName + "\"")
        .body(resource);
  }

  @GetMapping(value = "/video/lesson/{fileName}", produces = "video/mp4")
  public Mono<Resource> readVideoContent(@PathVariable("fileName") String fileName) {
    return fileStorageService.readVideoContent(fileName, Utils.VIDEO_UPLOAD_LESSON_URI);
  }

  @GetMapping(value = "/image/user/{fileName}")
  public ResponseEntity<byte[]> getImageUser(@PathVariable("fileName") String fileName) {
    byte[] image = imageStorageService.readFileContent(fileName, Utils.IMAGE_UPLOAD_USER_URI);
    return ResponseEntity.status(HttpStatus.OK)
        .contentType(MediaType.IMAGE_JPEG)
        .body(image);
  }

  @GetMapping(value = "/image/course/{fileName}")
  public ResponseEntity<byte[]> getImageCourse(@PathVariable("fileName") String fileName) {
    byte[] image = imageStorageService.readFileContent(fileName, Utils.IMAGE_UPLOAD_COURSE_URI);
    return ResponseEntity.status(HttpStatus.OK)
        .contentType(MediaType.IMAGE_JPEG)
        .body(image);
  }

  @GetMapping(value = "/image/question/{fileName}")
  public ResponseEntity<byte[]> getImageQuestion(@PathVariable("fileName") String fileName) {
    byte[] image = imageStorageService.readFileContent(fileName, Utils.IMAGE_UPLOAD_QUESTION_URI);
    return ResponseEntity.status(HttpStatus.OK)
        .contentType(MediaType.IMAGE_JPEG)
        .body(image);
  }

  @GetMapping(value = "/image/partExam/{fileName}")
  public ResponseEntity<byte[]> getImagePartExam(@PathVariable("fileName") String fileName) {
    byte[] image = imageStorageService.readFileContent(fileName, Utils.IMAGE_UPLOAD_PART_EXAM_URI);
    return ResponseEntity.status(HttpStatus.OK)
        .contentType(MediaType.IMAGE_JPEG)
        .body(image);
  }

  @GetMapping(value = "/audio/partExam/{fileName}", produces = "video/mp4")
  public Mono<Resource> getAudioPartExam(@PathVariable("fileName") String fileName) {
    return fileStorageService.readVideoContent(fileName, Utils.AUDIO_UPLOAD_PART_EXAM_URI);
  }
}
