package com.example.englishlearningwebsite.mapper;

import com.example.englishlearningwebsite.dto.response.QuestionResponseDTO;
import com.example.englishlearningwebsite.entities.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
  QuestionMapper INSTANCE = Mappers.getMapper( QuestionMapper.class );
  @Mapping(target = "id", source = "c.id")
  @Mapping(target = "content", source = "c.content")
  @Mapping(target = "serial", source = "c.serial")
  @Mapping(target = "document", source = "c.document")
  QuestionResponseDTO questionToQuestionResponseDTO(Question c);
}
