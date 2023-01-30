package com.example.englishlearningwebsite.service.implement;

import com.example.englishlearningwebsite.exception.FileException;
import com.example.englishlearningwebsite.exception.ResourceNotFoundException;
import com.example.englishlearningwebsite.service.FileStorageService;
import com.example.englishlearningwebsite.utils.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

@Service
public class FileStorageServiceImpl implements FileStorageService {
  @Autowired
  private ResourceLoader resourceLoader;

  private boolean isDocFile(MultipartFile file){
    String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
    assert fileExtension != null;
    return Arrays.asList(new String[] {"pdf", "doc", "docx"}).contains(fileExtension.trim().toLowerCase());
  }

  @Override
  public String storeFile(MultipartFile file, String uploadDir) {
    try{
      //Check file is empty
      if (file.isEmpty()){
        throw new FileException("File is empty");
      }

      //Check file is image
      /*if (!isDocFile(file)){
        throw new FileException("File isn't format ");
      }*/

      Path uploadPath = Paths.get("uploads/" + uploadDir);

      //Check path is exists
      if (!Files.exists(uploadPath)){
        uploadPath = Files.createDirectories(uploadPath);
      }

      Path destinationFilePath = uploadPath.resolve(Paths.get(file.getOriginalFilename())).normalize().toAbsolutePath();
      if (!destinationFilePath.getParent().equals(uploadPath.toAbsolutePath())){
        throw new FileException("Cannot store file outside current directory.");
      }

      //Copy file to destination file path
      try(InputStream inputStream = file.getInputStream();){
        Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
      } catch (Exception e){
        throw new FileException(e.getMessage());
      }

      return file.getOriginalFilename();
    } catch (Exception e){
      throw new FileException("Failed to store file " + e);
    }
  }

  @Override
  public Resource readFileContent(String fileName, String uploadDir) {
    try{
      Path uploadPath = Paths.get("uploads/" + uploadDir);
      Path path = uploadPath.resolve(fileName);
      Resource resource = new UrlResource(path.toUri());
      if (resource.exists() || resource.isReadable()){
        return resource;
      } else {
        throw new ResourceNotFoundException("Could not read file: " + fileName);
      }
    } catch (IOException e) {
      throw new FileException("Could not read file: " + fileName, e);
    }
  }

  @Override
  public void deleteFile(String fileName, String uploadDir) {
    try{
      Path uploadPath = Paths.get("uploads/" + uploadDir + fileName);
      FileSystemUtils.deleteRecursively(uploadPath.toFile());
    } catch (Exception e){
      throw new FileException(e.getMessage());
    }
  }

  @Override
  public Mono<Resource> readVideoContent(String fileName, String uploadDir) {
    Path uploadPath = Paths.get("uploads/" + uploadDir);
    Path path = uploadPath.resolve(fileName);
    try {
      Resource resource = new UrlResource(path.toUri());
      return Mono.fromSupplier(()->resource);
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }
}
