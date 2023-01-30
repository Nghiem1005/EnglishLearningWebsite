package com.example.englishlearningwebsite.service;

import com.example.englishlearningwebsite.dto.request.PartExamRequestDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface PartExamService {
  ResponseEntity<?> createPartExam(Long userId, PartExamRequestDTO partExamRequestDTO, MultipartFile document);
  ResponseEntity<?> updatePartExam(Long id, PartExamRequestDTO partExamRequestDTO, MultipartFile document);
  ResponseEntity<?> deletePartExam(Long id);
  ResponseEntity<?> getAllPartExam(Pageable pageable);
  ResponseEntity<?> getPartExamById(Long id);
}
