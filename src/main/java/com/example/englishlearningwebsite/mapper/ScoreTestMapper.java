package com.example.englishlearningwebsite.mapper;

import com.example.englishlearningwebsite.dto.response.ScoreExerciseResponseDTO;
import com.example.englishlearningwebsite.dto.response.ScoreTestResponseDTO;
import com.example.englishlearningwebsite.entities.Exercise;
import com.example.englishlearningwebsite.entities.Test;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ScoreTestMapper {
  ScoreTestMapper INSTANCE = Mappers.getMapper( ScoreTestMapper.class );

  @Mapping(target = "courseId", source = "c.course.id")
  @Mapping(target = "examId", source = "c.exam.id")
  @Mapping(target = "studentId", source = "c.student.id")
  @Mapping(target = "score", source = "c.scores")
  ScoreTestResponseDTO testToScoreTestResponseDTO(Test c);
}
