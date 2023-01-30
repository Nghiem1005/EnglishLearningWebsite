package com.example.englishlearningwebsite.service.implement;

import com.example.englishlearningwebsite.controller.ScheduleController;
import com.example.englishlearningwebsite.dto.request.LessonRequestDTO;
import com.example.englishlearningwebsite.dto.response.LessonResponseDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.entities.*;
import com.example.englishlearningwebsite.exception.InvalidValueException;
import com.example.englishlearningwebsite.exception.ResourceAlreadyExistsException;
import com.example.englishlearningwebsite.exception.ResourceNotFoundException;
import com.example.englishlearningwebsite.mapper.LessonMapper;
import com.example.englishlearningwebsite.repository.*;
import com.example.englishlearningwebsite.service.FileStorageService;
import com.example.englishlearningwebsite.service.LessonService;
import com.example.englishlearningwebsite.utils.Utils;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LessonServiceImpl implements LessonService {
  @Autowired private CourseRepository courseRepository;
  @Autowired private LessonRepository lessonRepository;
  @Autowired private FileStorageService fileStorageService;
  @Autowired private ExerciseRepository exerciseRepository;
  @Autowired private ScheduleRepository scheduleRepository;
  @Autowired private TopicRepository topicRepository;

  private static final String APPLICATION_NAME = "";
  private static HttpTransport httpTransport;
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  GoogleClientSecrets clientSecrets;
  //static GoogleAuthorizationCodeFlow flow = ScheduleController.fl;

  @Value("${google.client.client-id}")
  private String clientId;
  @Value("${google.client.client-secret}")
  private String clientSecret;
  @Value("${google.client.redirectUri}")
  private String redirectURI;

  //private final LessonMapper lessonMapper = Mappers.getMapper(LessonMapper.class);

  @Override
  public ResponseEntity<ResponseObject> getAllLessonByCourse(Pageable pageable, Long courseId) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

    Page<Lesson> lessonPage = lessonRepository.findLessonsByCourse(course, pageable);
    List<Lesson> lessonList = lessonPage.getContent();
    List<LessonResponseDTO> lessonResponseDTOArrayList = new ArrayList<>();
    for (Lesson lesson : lessonList) {
      LessonResponseDTO lessonResponseDTO = LessonMapper.INSTANCE.lessonToLessonResponseDTO(lesson);
      lessonResponseDTOArrayList.add(lessonResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List feedback.", lessonResponseDTOArrayList,
        lessonPage.getTotalPages()));
  }

  @Override
  public ResponseEntity<ResponseObject> createLesson(Long courseId,
      LessonRequestDTO lessonRequestDTO) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

    Lesson lesson = new Lesson();
    lesson.setCourse(course);

    lesson.setName(lessonRequestDTO.getName());
    if (lessonRequestDTO.getDocument() != null){
      String nameFile = fileStorageService.storeFile(lessonRequestDTO.getDocument(), Utils.VIDEO_UPLOAD_LESSON_URI);
      lesson.setDocument(nameFile);
    }
    //lesson.setDocument(lessonRequestDTO.getDocument());

    //Check serial greater previous serial and consecutive
    List<Lesson> lessonList = lessonRepository.findLessonByCourseOrderBySerial(course);
    if (lessonList.size() != 0){
      lesson.setSerial(lessonList.get(lessonList.size() - 1).getSerial() + 1);
    } else {
      lesson.setSerial(1);
    }

    LessonResponseDTO lessonResponseDTO = LessonMapper.INSTANCE.lessonToLessonResponseDTO(lessonRepository.save(lesson));

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create lesson success!", lessonResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> updateLesson(Long id, LessonRequestDTO lessonRequestDTO) {
    Lesson lesson = lessonRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find lesson with ID = " + id));

    lesson.setName(lessonRequestDTO.getName());
    //lesson.setDocument(lessonRequestDTO.getDocument());

    if (lessonRequestDTO.getDocument() != null){
      String nameFile = fileStorageService.storeFile(lessonRequestDTO.getDocument(), Utils.VIDEO_UPLOAD_LESSON_URI);
      lesson.setDocument(nameFile);
    }

    LessonResponseDTO lessonResponseDTO = LessonMapper.INSTANCE.lessonToLessonResponseDTO(lessonRepository.save(lesson));

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create lesson success!", lessonResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> deleteLesson(Long id, String code, GoogleAuthorizationCodeFlow flow, Calendar client)
      throws GeneralSecurityException, IOException {
    Lesson getLesson = lessonRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find feedback with ID = " + id));

    deleteExerciseAndScheduleAndTopicByLesson(getLesson, code, flow, client);
    lessonRepository.delete(getLesson);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Delete lesson successfully!"));
  }

  @Override
  public ResponseEntity<ResponseObject> getLessonById(Long id) {
    Lesson lesson = lessonRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find lesson with ID = " + id));
    LessonResponseDTO lessonResponseDTO = LessonMapper.INSTANCE.lessonToLessonResponseDTO(lesson);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Get lesson.", lessonResponseDTO));
  }

  public void deleteExerciseAndScheduleAndTopicByLesson(Lesson lesson, String code, GoogleAuthorizationCodeFlow flow, Calendar client)
      throws IOException, GeneralSecurityException {
    //Delete exercise
    List<Exercise> exerciseList = exerciseRepository.findExercisesByLesson(lesson);
    if (exerciseList.size() != 0){
      exerciseRepository.deleteAll(exerciseList);
    }

    //Delete topic
    List<Topic> topicList = topicRepository.findTopicByLesson(lesson);
    topicRepository.deleteAll(topicList);

    //Delete schedule
    Optional<Schedule> getSchedule = scheduleRepository.findScheduleByLesson(lesson);
    if (getSchedule.isPresent()){
      Schedule schedule = getSchedule.get();

      //Delete event on calendar
      /*TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectURI).execute();
      Credential credential = flow.createAndStoreCredential(response, "userID");
      HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
      Calendar client = new Calendar.Builder(httpTransport, JSON_FACTORY,
          credential)
          .setApplicationName(APPLICATION_NAME).build();*/
      client.events().delete("primary", schedule.getMeetId()).execute();

      scheduleRepository.delete(schedule);
    }
  }
}
