package com.example.englishlearningwebsite.mapper;

import com.example.englishlearningwebsite.dto.response.StudentCourseResponseDTO;
import com.example.englishlearningwebsite.entities.StudentCourse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StudentCourseMapper {
  StudentCourseMapper INSTANCE = Mappers.getMapper( StudentCourseMapper.class );
  @Mapping(target = "studentId", source = "c.user.id")
  @Mapping(target = "studentName", source = "c.user.name")
  @Mapping(target = "courseId", source = "c.course.id")
  @Mapping(target = "courseName", source = "c.course.name")
  @Mapping(target = "teacherId", source = "c.course.teacher.id")
  @Mapping(target = "teacherName", source = "c.course.teacher.name")
  @Mapping(target = "progress", source = "c.progress")
  StudentCourseResponseDTO studentCourseToStudentCourseResponseDTO(StudentCourse c);
}
