package com.example.englishlearningwebsite.mapper;

import com.example.englishlearningwebsite.dto.response.PartExamResponseDTO;
import com.example.englishlearningwebsite.entities.PartExam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PartExamMapper {
  PartExamMapper INSTANCE = Mappers.getMapper( PartExamMapper.class );
  @Mapping(target = "id", source = "c.id")
  @Mapping(target = "name", source = "c.name")
  @Mapping(target = "type", source = "c.type")
  PartExamResponseDTO partExamToPartExamResponseDTO(PartExam c);
}
