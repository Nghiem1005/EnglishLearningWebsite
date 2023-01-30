package com.example.englishlearningwebsite.controller;

import com.example.englishlearningwebsite.dto.request.PartExamRequestDTO;
import com.example.englishlearningwebsite.dto.request.PracticeRequestDTO;
import com.example.englishlearningwebsite.service.ExamDetailService;
import com.example.englishlearningwebsite.service.PartExamService;
import com.example.englishlearningwebsite.service.PracticeService;
import com.example.englishlearningwebsite.utils.Utils;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/v1/partExam")
public class PartExamController {
  @Autowired
  private PartExamService partExamService;
  @Autowired private ExamDetailService examDetailService;
  @PostMapping(value = "", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @RequestBody(content = @Content(encoding = @Encoding(name = "partExamRequestDTO", contentType = "application/json")))
  public ResponseEntity<?> createPartExam(@RequestParam(name = "creatorId") Long creatorId, @RequestPart PartExamRequestDTO partExamRequestDTO,
      @RequestPart MultipartFile document) {
    return partExamService.createPartExam(creatorId, partExamRequestDTO, document);
  }

  @PutMapping(value = "", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @RequestBody(content = @Content(encoding = @Encoding(name = "partExamRequestDTO", contentType = "application/json")))
  public ResponseEntity<?> updatePartExam(@RequestParam(name = "id") Long id, @RequestPart PartExamRequestDTO partExamRequestDTO,
      @RequestPart(required = false) MultipartFile document) {
    return partExamService.updatePartExam(id, partExamRequestDTO, document);
  }

  @DeleteMapping(value = "")
  public ResponseEntity<?> deletePartExam(@RequestParam(name = "id") Long id) {
    return partExamService.deletePartExam(id);
  }

  @GetMapping(value = "")
  public ResponseEntity<?> getAllPartExam(@RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page){
    Pageable pageable = PageRequest.of(page - 1, size);
    return partExamService.getAllPartExam(pageable);
  }

  @GetMapping(value = "{id}")
  public ResponseEntity<?> getPartExamById(@PathVariable(name = "id") Long id){
    return partExamService.getPartExamById(id);
  }

  @GetMapping(value = "/exam")
  public ResponseEntity<?> getPartExamByExam(@RequestParam(name = "examId") Long examId){
    return examDetailService.getPartExamByExam(examId);
  }
}
