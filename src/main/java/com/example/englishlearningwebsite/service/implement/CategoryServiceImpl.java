package com.example.englishlearningwebsite.service.implement;

import com.example.englishlearningwebsite.dto.response.LevelResponseDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.entities.enums.CourseType;
import com.example.englishlearningwebsite.entities.enums.Level;
import com.example.englishlearningwebsite.service.CategoryService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

  @Override
  public ResponseEntity<?> getLevel() {
    List<LevelResponseDTO> levels = new ArrayList<>();
    Arrays.stream(Level.values()).forEach(level -> {
      LevelResponseDTO levelResponseDTO = new LevelResponseDTO();
      levelResponseDTO.setName(level.name());
      levelResponseDTO.setValue(level.value());
      levels.add(levelResponseDTO);
    });

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Course level", levels.toArray()));
  }

  @Override
  public ResponseEntity<?> getCourseType() {
    List<CourseType> courseType = new ArrayList<>();
    Arrays.stream(CourseType.values()).forEach(type -> courseType.add(type));
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Course type", courseType.toArray()));
  }
}
