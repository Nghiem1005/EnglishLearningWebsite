package com.example.englishlearningwebsite.service.implement;

import com.example.englishlearningwebsite.exception.FileException;
import com.example.englishlearningwebsite.exception.ResourceNotFoundException;
import com.example.englishlearningwebsite.service.ImageStorageService;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class ImageStorageServiceImpl implements ImageStorageService {

  private boolean isImageFile(MultipartFile file){
    String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
    assert fileExtension != null;
    return Arrays.asList(new String[] {"png", "jpg", "jpeg", "bmp"}).contains(fileExtension.trim().toLowerCase());
  }

  @Override
  public String storeFile(MultipartFile file, String uploadDir) {
    try{
      log.info("Checking file... ");

      //Check file is empty
      if (file.isEmpty()){
        throw new FileException("File is empty");
      }

      //Check file is image
      if (!isImageFile(file)){
        throw new FileException("File isn't image ");
      }

      //Check file size less 5MB
      float fileSize = file.getSize() / 1_000_000.0f;
      if (fileSize > 5.0f){
        throw new FileException("File size must be less than 5MB");
      }

      Path uploadPath = Paths.get("uploads/" + uploadDir);

      //Check path is exists
      if (!Files.exists(uploadPath)){
        uploadPath = Files.createDirectories(uploadPath);
      }

      //File must rename before storage
      String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
      String generateName = UUID.randomUUID().toString().replace("-", "");
      generateName = generateName + "." + fileExtension;
      Path destinationFilePath = uploadPath.resolve(Paths.get(generateName)).normalize().toAbsolutePath();
      if (!destinationFilePath.getParent().equals(uploadPath.toAbsolutePath())){
        throw new FileException("Cannot store file outside current directory.");
      }

      //Copy file to destination file path
      try(InputStream inputStream = file.getInputStream();){
        Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
      } catch (Exception e){
        throw new FileException(e.getMessage());
      }

      return generateName;
    } catch (Exception e){
      throw new FileException("Failed to store file " + e);
    }
  }

  @Override
  public byte[] readFileContent(String fileName, String uploadDir) {
    try{
      Path uploadPath = Paths.get("uploads/" + uploadDir);
      Path path = uploadPath.resolve(fileName);
      Resource resource = new UrlResource(path.toUri());
      if (resource.exists() || resource.isReadable()){
        byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
        return bytes;
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
}
