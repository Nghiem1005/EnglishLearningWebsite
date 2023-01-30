package com.example.englishlearningwebsite.service.implement;

import com.example.englishlearningwebsite.dto.response.FeedbackResponseDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.entities.Course;
import com.example.englishlearningwebsite.entities.Feedback;
import com.example.englishlearningwebsite.entities.StudentCourse;
import com.example.englishlearningwebsite.entities.User;
import com.example.englishlearningwebsite.exception.ResourceNotFoundException;
import com.example.englishlearningwebsite.mapper.FeedbackMapper;
import com.example.englishlearningwebsite.repository.CourseRepository;
import com.example.englishlearningwebsite.repository.FeedbackRepository;
import com.example.englishlearningwebsite.repository.StudentCourseRepository;
import com.example.englishlearningwebsite.repository.UserRepository;
import com.example.englishlearningwebsite.service.FeedbackService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class FeedbackServiceImpl implements FeedbackService {
  @Autowired private CourseRepository courseRepository;
  //private final FeedbackMapper mapper = Mappers.getMapper(FeedbackMapper.class);
  @Autowired
  private FeedbackRepository feedbackRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private StudentCourseRepository studentCourseRepository;

  @Override
  public ResponseEntity<ResponseObject> getAllFeedback(Pageable pageable) {
    Page<Feedback> getFeedbackList = feedbackRepository.findAll(pageable);
    List<Feedback> feedbackList = getFeedbackList.getContent();

    //Convert feedback to feedback response dto
    List<FeedbackResponseDTO> feedbackResponseDTOList = new ArrayList<>();
    for (Feedback feedback : feedbackList) {
      FeedbackResponseDTO feedbackResponseDTO = FeedbackMapper.INSTANCE.feedbackToFeedbackResponseDTO(feedback);
      feedbackResponseDTOList.add(feedbackResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List feedback.", feedbackResponseDTOList,
        getFeedbackList.getTotalPages()));
  }

  @Override
  public ResponseEntity<ResponseObject> getAllFeedbackByCourse(Pageable pageable, Long courseId) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

    Page<Feedback> getFeedbackList = feedbackRepository.findFeedbacksByCourse(pageable, course);
    List<Feedback> feedbackList = getFeedbackList.getContent();

    //Convert feedback to feedback response dto
    List<FeedbackResponseDTO> feedbackResponseDTOList = new ArrayList<>();
    for (Feedback feedback : feedbackList) {
      FeedbackResponseDTO feedbackResponseDTO = FeedbackMapper.INSTANCE.feedbackToFeedbackResponseDTO(feedback);
      feedbackResponseDTOList.add(feedbackResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List feedback.", feedbackResponseDTOList,
        getFeedbackList.getTotalPages()));
  }

  @Override
  public ResponseEntity<ResponseObject> createFeedback(Long studentId, Long courseId, String content) {
    Feedback feedback = new Feedback();
    feedback.setContent(content);

    User student = userRepository.findById(studentId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + studentId));
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));
    Optional<StudentCourse> getStudentCourse = studentCourseRepository.findStudentCourseByCourseAndUser(course, student);

    //Check if the student has taken this course or not
    if (!getStudentCourse.isPresent()){
      throw new ResourceNotFoundException("Students who have never taken this course ");
    }

    feedback.setCourse(course);
    feedback.setUser(student);

    Feedback feedbackSaved = feedbackRepository.save(feedback);
    FeedbackResponseDTO feedbackResponseDTO = FeedbackMapper.INSTANCE.feedbackToFeedbackResponseDTO(feedbackSaved);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Create feedback successfully!", feedbackResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> updateFeedback(String content, Long id) {
    Feedback feedback = feedbackRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find feedback with ID = " + id));
    feedback.setId(id);
    feedback.setContent(content);
    Feedback feedbackSaved = feedbackRepository.save(feedback);
    FeedbackResponseDTO feedbackResponseDTO = FeedbackMapper.INSTANCE.feedbackToFeedbackResponseDTO(feedbackSaved);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Update feedback successfully!", feedbackResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> deleteFeedback(Long id) {
    Feedback getFeedback = feedbackRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find feedback with ID = " + id));

    feedbackRepository.delete(getFeedback);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Delete feedback successfully!"));
  }

  @Override
  public ResponseEntity<ResponseObject> getFeedbackById(Long id) {
    Feedback feedback = feedbackRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find feedback with ID = " + id));
    FeedbackResponseDTO feedbackResponseDTO = FeedbackMapper.INSTANCE.feedbackToFeedbackResponseDTO(feedback);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Get feedback.", feedbackResponseDTO));
  }

}
