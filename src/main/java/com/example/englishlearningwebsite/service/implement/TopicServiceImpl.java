package com.example.englishlearningwebsite.service.implement;

import com.example.englishlearningwebsite.dto.request.TopicRequestDTO;
import com.example.englishlearningwebsite.dto.response.LessonResponseDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.dto.response.TopicResponseDTO;
import com.example.englishlearningwebsite.entities.Course;
import com.example.englishlearningwebsite.entities.Lesson;
import com.example.englishlearningwebsite.entities.Topic;
import com.example.englishlearningwebsite.exception.ResourceAlreadyExistsException;
import com.example.englishlearningwebsite.exception.ResourceNotFoundException;
import com.example.englishlearningwebsite.mapper.TopicMapper;
import com.example.englishlearningwebsite.repository.LessonRepository;
import com.example.englishlearningwebsite.repository.TopicRepository;
import com.example.englishlearningwebsite.service.TopicService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TopicServiceImpl implements TopicService {
  @Autowired private LessonRepository lessonRepository;
  @Autowired private TopicRepository topicRepository;
  //private final TopicMapper topicMapper = Mappers.getMapper(TopicMapper.class);

  @Override
  public ResponseEntity<ResponseObject> getAllTopicByLesson(Long lessonId, Pageable pageable) {
    Lesson lesson = lessonRepository.findById(lessonId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find lesson with ID = " + lessonId));

    Page<Topic> topicPage = topicRepository.findTopicByLesson(lesson, pageable);
    List<Topic> topicList = topicPage.getContent();
    List<TopicResponseDTO> topicResponseDTOList = new ArrayList<>();
    for (Topic topic : topicList) {
      TopicResponseDTO topicResponseDTO = TopicMapper.INSTANCE.topicToTopicResponseDTO(topic);
      topicResponseDTOList.add(topicResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List feedback.", topicResponseDTOList,
        topicPage.getTotalPages()));
  }

  @Override
  public ResponseEntity<ResponseObject> createTopic(Long lessonId,
      TopicRequestDTO topicRequestDTO) {
    Lesson lesson = lessonRepository.findById(lessonId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find lesson with ID = " + lessonId));

    Topic topic = new Topic();
    topic.setLesson(lesson);

    //Check if name lesson in course or no
    Optional<Topic> getTopic = topicRepository.findTopicByTitle(topicRequestDTO.getTitle());
    if (getTopic.isPresent()){
      if (getTopic.get().getLesson() == lesson){
        throw new ResourceAlreadyExistsException("This course has already lesson.");
      }
    }

    topic.setTitle(topicRequestDTO.getTitle());
    topic.setContent(topicRequestDTO.getContent());

    TopicResponseDTO toTopicResponseDTO = TopicMapper.INSTANCE.topicToTopicResponseDTO(topicRepository.save(topic));

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create topic success!", toTopicResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> updateTopic(Long id, TopicRequestDTO topicRequestDTO) {
    Topic topic = topicRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find topic with ID = " + id));

    //Check if name topic in lesson or no
    Optional<Topic> getTopic = topicRepository.findTopicByTitle(topicRequestDTO.getTitle());
    if (getTopic.isPresent()){
      if (getTopic.get().getLesson() == topic.getLesson() && getTopic.get().getId() != id){
        throw new ResourceAlreadyExistsException("This course has already lesson.");
      }
    }

    topic.setTitle(topicRequestDTO.getTitle());
    topic.setContent(topicRequestDTO.getContent());

    TopicResponseDTO toTopicResponseDTO = TopicMapper.INSTANCE.topicToTopicResponseDTO(topicRepository.save(topic));

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create topic success!", toTopicResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> deleteTopic(Long id) {
    Topic getTopic = topicRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find feedback with ID = " + id));

    topicRepository.delete(getTopic);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Delete feedback successfully!"));
  }
}
