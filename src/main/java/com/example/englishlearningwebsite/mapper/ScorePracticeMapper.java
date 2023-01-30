package com.example.englishlearningwebsite.mapper;

import com.example.englishlearningwebsite.dto.response.ScorePracticeResponseDTO;
import com.example.englishlearningwebsite.dto.response.ScoreTestResponseDTO;
import com.example.englishlearningwebsite.entities.Practice;
import com.example.englishlearningwebsite.entities.Test;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ScorePracticeMapper {
  ScorePracticeMapper INSTANCE = Mappers.getMapper( ScorePracticeMapper.class );

  @Mapping(target = "examId", source = "c.exam.id")
  @Mapping(target = "studentId", source = "c.student.id")
  @Mapping(target = "score", source = "c.scores")
  ScorePracticeResponseDTO practiceToScorePracticeResponseDTO(Practice c);
}
