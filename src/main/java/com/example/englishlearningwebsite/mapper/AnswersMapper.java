package com.example.englishlearningwebsite.mapper;

import com.example.englishlearningwebsite.dto.response.AnswerResponseDTO;
import com.example.englishlearningwebsite.entities.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AnswersMapper {
  AnswersMapper INSTANCE = Mappers.getMapper( AnswersMapper.class );
  @Mapping(target = "id", source = "c.id")
  @Mapping(target = "content", source = "c.content")
  @Mapping(target = "correct", source = "c.correct")
  @Mapping(target = "questionId", source = "c.question.id")
  @Mapping(target = "questionContent", source = "c.question.content")
  AnswerResponseDTO answerToAnswerResponseDTO(Answer c);
}
