package com.example.englishlearningwebsite.service.implement;

import com.example.englishlearningwebsite.dto.request.CourseRequestDTO;
import com.example.englishlearningwebsite.dto.response.CourseResponseDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.dto.response.StatisticInfoCourseResponseDTO;
import com.example.englishlearningwebsite.dto.response.UserResponseDTO;
import com.example.englishlearningwebsite.entities.*;
import com.example.englishlearningwebsite.entities.enums.CourseType;
import com.example.englishlearningwebsite.entities.enums.Level;
import com.example.englishlearningwebsite.exception.ResourceNotFoundException;
import com.example.englishlearningwebsite.mapper.CourseMapper;
import com.example.englishlearningwebsite.mapper.UserMapper;
import com.example.englishlearningwebsite.repository.*;
import com.example.englishlearningwebsite.service.CourseService;
import com.example.englishlearningwebsite.service.FileStorageService;
import com.example.englishlearningwebsite.service.ImageStorageService;
import com.example.englishlearningwebsite.service.LessonService;
import com.example.englishlearningwebsite.utils.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CourseServiceImpl implements CourseService {
  @Autowired private CourseRepository courseRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private FileStorageService fileStorageService;
  @Autowired private StudentCourseRepository studentCourseRepository;
  @Autowired private FeedbackRepository feedbackRepository;
  @Autowired private BillRepository billRepository;
  @Autowired private ImageStorageService imageStorageService;
  @Autowired private TestRepository testRepository;
  @Autowired private LessonRepository lessonRepository;
  @Autowired private ExerciseRepository exerciseRepository;
  @Autowired private ScheduleRepository scheduleRepository;
  @Autowired private TopicRepository topicRepository;

  private static final String APPLICATION_NAME = "";
  private static HttpTransport httpTransport;
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  GoogleClientSecrets clientSecrets;
  //static GoogleAuthorizationCodeFlow flow;

  @Value("${google.client.client-id}")
  private String clientId;
  @Value("${google.client.client-secret}")
  private String clientSecret;
  @Value("${google.client.redirectUri}")
  private String redirectURI;

  //private final CourseMapper courseMapper = Mappers.getMapper(CourseMapper.class);

  @Override
  public ResponseEntity<ResponseObject> saveCourse(Long teacherId,
      CourseRequestDTO courseRequestDTO) {
    User teacher = userRepository.findById(teacherId).orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + teacherId));
    Course course = new Course();

    course.setTeacher(teacher);
    course.setName(courseRequestDTO.getName());
    course.setPrice(courseRequestDTO.getPrice());
    course.setDescription(courseRequestDTO.getDescription());
    course.setCourseType(CourseType.valueOf(courseRequestDTO.getCourseType().toUpperCase()));
    course.setLevel(Level.valueOf(courseRequestDTO.getLevel().toUpperCase()));

    //Store document
    if (courseRequestDTO.getDocuments() != null){
      List<String> nameFiles = storeFile(course, courseRequestDTO.getDocuments());
      course.setDocument(nameFiles);
    }

    //Store thumbnail
    if (courseRequestDTO.getThumbnail() != null){
      String nameFiles = imageStorageService.storeFile(courseRequestDTO.getThumbnail(), Utils.IMAGE_UPLOAD_COURSE_URI);
      course.setThumbnail(nameFiles);
    }

    Course courseSaved = courseRepository.save(course);

    CourseResponseDTO courseResponseDTO = CourseMapper.INSTANCE.courseToCourseResponseDTO(courseSaved);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create course success!", courseResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> updateCourse(Long courseId,
      CourseRequestDTO courseRequestDTO) {
    Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

    course.setPrice(courseRequestDTO.getPrice());
    course.setDescription(courseRequestDTO.getDescription());
    course.setCourseType(CourseType.valueOf(courseRequestDTO.getCourseType().toUpperCase()));
    course.setLevel(Level.valueOf(courseRequestDTO.getLevel().toUpperCase()));

    //Check name
    if (!courseRequestDTO.getName().equals(course.getName())){
      course.setName(courseRequestDTO.getName());
    }

    //Store document
    if (courseRequestDTO.getDocuments() != null){
      List<String> nameFiles = storeFile(course, courseRequestDTO.getDocuments());
      course.setDocument(nameFiles);
    }

    //Store thumbnail
    if (courseRequestDTO.getThumbnail() != null){
      String nameFiles = imageStorageService.storeFile(courseRequestDTO.getThumbnail(), Utils.IMAGE_UPLOAD_COURSE_URI);
      course.setThumbnail(nameFiles);
    }

    Course courseSaved = courseRepository.save(course);

    CourseResponseDTO courseResponseDTO = CourseMapper.INSTANCE.courseToCourseResponseDTO(courseSaved);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Update course success!", courseResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> deleteCourse(Long courseId, String code, GoogleAuthorizationCodeFlow flow, Calendar client)
      throws GeneralSecurityException, IOException {
    Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

    //Update course on bill to null
    List<Bill> billList = billRepository.findBillsByCourse(course);
    for (Bill bill : billList){
      bill.setCourse(null);
      billRepository.save(bill);
    }
    deleteFile(course);

    //Delete test by course
    List<Test> testList = testRepository.findTestsByCourse(course);
    testRepository.deleteAll(testList);

    //Delete student course
    List<StudentCourse> studentCourseList = studentCourseRepository.findStudentCoursesByCourse(course);
    studentCourseRepository.deleteAll(studentCourseList);

    //Delete lesson
    List<Lesson> lessonList = lessonRepository.findLessonsByCourse(course);
    for (Lesson lesson : lessonList){
      deleteExerciseAndScheduleAndTopicByLesson(lesson, code, flow, client);
      lessonRepository.delete(lesson);
    }

    courseRepository.delete(course);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Delete course success!"));
  }

  @Override
  public ResponseEntity<?> getAllCourse(Pageable pageable) {
    Page<Course> getCourseList = courseRepository.findAll(pageable);
    List<Course> courseList = getCourseList.getContent();

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List course!", toCourseResponseDTOList(courseList), getCourseList.getTotalPages()));
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

  @Override
  public ResponseEntity<ResponseObject> getCourseById(Long id) {
    Course course = courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + id));

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Get a course!", toCourseResponseDTO(course)));
  }

  @Override
  public ResponseEntity<ResponseObject> getCourseByTeacher(Pageable pageable, Long teacherId) {
    User teacher = userRepository.findById(teacherId).orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + teacherId));
    Page<Course> getCourseList = courseRepository.findCoursesByTeacher(pageable, teacher);
    List<Course> courseList = getCourseList.getContent();

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List course!", toCourseResponseDTOList(courseList), getCourseList.getTotalPages()));
  }

  @Override
  public ResponseEntity<ResponseObject> getCourseByNotTeacher(Pageable pageable) {
    Page<Course> getCourseList = courseRepository.findCoursesByTeacherIsNull(pageable);
    List<Course> courseList = getCourseList.getContent();

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List course!", toCourseResponseDTOList(courseList), getCourseList.getTotalPages()));
  }

  @Override
  public ResponseEntity<?> getCourseByType(Pageable pageable, String type) {
    Page<Course> getCourseList = courseRepository.findCoursesByCourseType(pageable, type);
    List<Course> courseList = getCourseList.getContent();

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List course!", toCourseResponseDTOList(courseList), getCourseList.getTotalPages()));
  }

  private List<CourseResponseDTO> toCourseResponseDTOList(List<Course> courseList){
    List<CourseResponseDTO> courseResponseDTOList = new ArrayList<>();
    for (Course course : courseList){
      courseResponseDTOList.add(toCourseResponseDTO(course));
    }
    return courseResponseDTOList;
  }

  private CourseResponseDTO toCourseResponseDTO(Course course){
    CourseResponseDTO courseResponseDTO = CourseMapper.INSTANCE.courseToCourseResponseDTO(course);

    //Set document
    List<String> documentCourseList = course.getDocument();
    String[] documentCourseArray = new String[documentCourseList.size()];
    for (int i=0; i<documentCourseList.size(); i++){
      documentCourseArray[i] = documentCourseList.get(i);
    }
    courseResponseDTO.setDocument(documentCourseArray);

    //Set teacher response
    UserResponseDTO userResponseDTO = UserMapper.INSTANCE.userToUserResponseDTO(course.getTeacher());
    courseResponseDTO.setTeacher(userResponseDTO);

    return courseResponseDTO;
  }

  private List<String> storeFile(Course course, MultipartFile[] documents){
    int numberOfDocument = documents.length;
    String[] nameFiles = new String[numberOfDocument];
    for (int i=0; i<numberOfDocument; i++){
      nameFiles[i] = fileStorageService.storeFile(documents[i], Utils.FILE_UPLOAD_COURSE_URI);
    }

    return new ArrayList<>(Arrays.asList(nameFiles));
  }

  private void deleteFile(Course course){
    for (String nameFile : course.getDocument()){
      fileStorageService.deleteFile(nameFile, Utils.FILE_UPLOAD_COURSE_URI);
    }
  }
}
