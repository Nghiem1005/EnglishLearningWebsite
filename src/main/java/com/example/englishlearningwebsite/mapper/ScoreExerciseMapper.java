package com.example.englishlearningwebsite.mapper;

import com.example.englishlearningwebsite.dto.response.ScoreExerciseResponseDTO;
import com.example.englishlearningwebsite.dto.response.StudentCourseResponseDTO;
import com.example.englishlearningwebsite.entities.Exercise;
import com.example.englishlearningwebsite.entities.StudentCourse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ScoreExerciseMapper {
  ScoreExerciseMapper INSTANCE = Mappers.getMapper( ScoreExerciseMapper.class );
  @Mapping(target = "lessonId", source = "c.lesson.id")
  @Mapping(target = "examId", source = "c.exam.id")
  @Mapping(target = "studentId", source = "c.student.id")
  @Mapping(target = "score", source = "c.scores")
  ScoreExerciseResponseDTO exerciseToScoreExerciseResponseDTO(Exercise c);
}
