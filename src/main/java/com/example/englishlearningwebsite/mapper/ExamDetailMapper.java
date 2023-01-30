package com.example.englishlearningwebsite.mapper;

import com.example.englishlearningwebsite.dto.response.ExamDetailResponseDTO;
import com.example.englishlearningwebsite.dto.response.PartExamResponseDTO;
import com.example.englishlearningwebsite.entities.ExamDetail;
import com.example.englishlearningwebsite.entities.PartExam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ExamDetailMapper {
  ExamDetailMapper INSTANCE = Mappers.getMapper( ExamDetailMapper.class );

  @Mapping(target = "examId", source = "c.exam.id")
  @Mapping(target = "serial", source = "c.serial")
  ExamDetailResponseDTO examDetailToExamDetailResponseDTO(ExamDetail c);
}
