package com.example.englishlearningwebsite.mapper;

import com.example.englishlearningwebsite.dto.response.LessonResponseDTO;
import com.example.englishlearningwebsite.dto.response.TopicResponseDTO;
import com.example.englishlearningwebsite.entities.Lesson;
import com.example.englishlearningwebsite.entities.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TopicMapper {
  TopicMapper INSTANCE = Mappers.getMapper( TopicMapper.class );
  @Mapping(target = "id", source = "c.id")
  @Mapping(target = "title", source = "c.title")
  @Mapping(target = "content", source = "c.content")
  @Mapping(target = "lessonId", source = "c.lesson.id")
  @Mapping(target = "lessonName", source = "c.lesson.name")
  TopicResponseDTO topicToTopicResponseDTO(Topic c);
}
