package com.example.englishlearningwebsite.mapper;

import com.example.englishlearningwebsite.dto.response.FeedbackResponseDTO;
import com.example.englishlearningwebsite.entities.Feedback;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {
  FeedbackMapper INSTANCE = Mappers.getMapper( FeedbackMapper.class );
  @Mapping(target = "id", source = "feedback.id")
  @Mapping(target = "content", source = "feedback.content")
  @Mapping(target = "studentId", source = "feedback.user.id")
  @Mapping(target = "courseId", source = "feedback.course.id")
  @Mapping(target = "createDate", source = "feedback.createDate")
  @Mapping(target = "updateDate", source = "feedback.updateDate")
  @Mapping(target = "studentName", source = "feedback.user.name")
  @Mapping(target = "courseName", source = "feedback.course.name")
  FeedbackResponseDTO feedbackToFeedbackResponseDTO(Feedback feedback);
}