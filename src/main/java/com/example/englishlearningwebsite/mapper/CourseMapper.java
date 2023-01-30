package com.example.englishlearningwebsite.mapper;

import com.example.englishlearningwebsite.dto.request.UserRequestDTO;
import com.example.englishlearningwebsite.dto.response.CourseResponseDTO;
import com.example.englishlearningwebsite.entities.Course;
import com.example.englishlearningwebsite.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CourseMapper {
  CourseMapper INSTANCE = Mappers.getMapper( CourseMapper.class );
  @Mapping(target = "id", source = "c.id")
  @Mapping(target = "name", source = "c.name")
  @Mapping(target = "price", source = "c.price")
  @Mapping(target = "description", source = "c.description")
  @Mapping(target = "thumbnail", source = "c.thumbnail")
  @Mapping(target = "level", source = "c.level")
  @Mapping(target = "courseType", source = "c.courseType")
  CourseResponseDTO courseToCourseResponseDTO(Course c);
}
