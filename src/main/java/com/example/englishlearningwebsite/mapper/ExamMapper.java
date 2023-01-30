package com.example.englishlearningwebsite.mapper;

import com.example.englishlearningwebsite.dto.response.ExamResponseDTO;
import com.example.englishlearningwebsite.dto.response.PartExamResponseDTO;
import com.example.englishlearningwebsite.entities.Exam;
import com.example.englishlearningwebsite.entities.PartExam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ExamMapper {
  ExamMapper INSTANCE = Mappers.getMapper( ExamMapper.class );

  @Mapping(target = "id", source = "c.id")
  @Mapping(target = "name", source = "c.name")
  @Mapping(target = "time", source = "c.time")
  ExamResponseDTO examToExamResponseDTO(Exam c);
}
