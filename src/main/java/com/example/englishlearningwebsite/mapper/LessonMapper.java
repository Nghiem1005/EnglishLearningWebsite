package com.example.englishlearningwebsite.mapper;

import com.example.englishlearningwebsite.dto.response.LessonResponseDTO;
import com.example.englishlearningwebsite.entities.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LessonMapper {
  LessonMapper INSTANCE = Mappers.getMapper( LessonMapper.class );
  @Mapping(target = "id", source = "c.id")
  @Mapping(target = "name", source = "c.name")
  @Mapping(target = "courseId", source = "c.course.id")
  @Mapping(target = "courseName", source = "c.course.name")
  @Mapping(target = "teacherId", source = "c.course.teacher.id")
  @Mapping(target = "teacherName", source = "c.course.teacher.name")
  @Mapping(target = "document", source = "c.document")
  LessonResponseDTO lessonToLessonResponseDTO(Lesson c);
}
