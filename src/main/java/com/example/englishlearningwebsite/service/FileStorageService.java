package com.example.englishlearningwebsite.service;

import java.net.MalformedURLException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

public interface FileStorageService {
  String storeFile(MultipartFile file, String uploadDir);
  Resource readFileContent(String fileName, String uploadDir);
  void deleteFile(String fileName, String uploadDir);
  Mono<Resource> readVideoContent(String fileName, String uploadDir);
}
