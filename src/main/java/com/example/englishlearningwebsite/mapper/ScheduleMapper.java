package com.example.englishlearningwebsite.mapper;

import com.example.englishlearningwebsite.dto.response.ScheduleResponseDTO;
import com.example.englishlearningwebsite.dto.response.ScoreTestResponseDTO;
import com.example.englishlearningwebsite.entities.Schedule;
import com.example.englishlearningwebsite.entities.Test;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
  ScheduleMapper INSTANCE = Mappers.getMapper( ScheduleMapper.class );

  @Mapping(target = "id", source = "c.id")
  @Mapping(target = "link", source = "c.link")
  @Mapping(target = "title", source = "c.title")
  @Mapping(target = "startDate", source = "c.startDate")
  @Mapping(target = "endDate", source = "c.endDate")
  @Mapping(target = "lessonId", source = "c.lesson.id")
  ScheduleResponseDTO scheduleToScheduleResponseDTO(Schedule c);
}
