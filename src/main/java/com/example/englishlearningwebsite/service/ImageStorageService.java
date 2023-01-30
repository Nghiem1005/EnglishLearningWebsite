package com.example.englishlearningwebsite.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {
  String storeFile(MultipartFile file, String uploadDir);

  byte[] readFileContent(String fileName, String uploadDir);

  void deleteFile(String fileName, String uploadDir);
}
