package com.example.englishlearningwebsite.service.implement;

import com.example.englishlearningwebsite.controller.ScheduleController;
import com.example.englishlearningwebsite.dto.request.CalendarRequestDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.dto.response.ScheduleResponseDTO;
import com.example.englishlearningwebsite.entities.Course;
import com.example.englishlearningwebsite.entities.Lesson;
import com.example.englishlearningwebsite.entities.Schedule;
import com.example.englishlearningwebsite.entities.StudentCourse;
import com.example.englishlearningwebsite.entities.User;
import com.example.englishlearningwebsite.exception.InvalidValueException;
import com.example.englishlearningwebsite.exception.ResourceAlreadyExistsException;
import com.example.englishlearningwebsite.exception.ResourceNotFoundException;
import com.example.englishlearningwebsite.mapper.ScheduleMapper;
import com.example.englishlearningwebsite.repository.CourseRepository;
import com.example.englishlearningwebsite.repository.LessonRepository;
import com.example.englishlearningwebsite.repository.ScheduleRepository;
import com.example.englishlearningwebsite.repository.StudentCourseRepository;
import com.example.englishlearningwebsite.repository.UserRepository;
import com.example.englishlearningwebsite.service.ScheduleService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.ConferenceData;
import com.google.api.services.calendar.model.ConferenceSolutionKey;
import com.google.api.services.calendar.model.CreateConferenceRequest;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.google.api.services.calendar.Calendar;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ScheduleServiceImpl implements ScheduleService {

  @Autowired
  private LessonRepository lessonRepository;
  @Autowired
  private StudentCourseRepository studentCourseRepository;
  @Autowired private ScheduleRepository scheduleRepository;
  @Autowired private CourseRepository courseRepository;
  @Autowired private UserRepository userRepository;

  @Override
  public ResponseEntity<ResponseObject> createSchedule(Long lessonId, String code,
      CalendarRequestDTO calendarRequestDTO, GoogleAuthorizationCodeFlow flow,
      GoogleClientSecrets clientSecrets, Calendar client) {
    Lesson lesson = lessonRepository.findById(lessonId)
        .orElseThrow(
            () -> new ResourceNotFoundException("Could not find lesson with ID = " + lessonId));

    Schedule schedule = new Schedule();

    //Check schedule already by lesson
    Optional<Schedule> getSchedule = scheduleRepository.findScheduleByLesson(lesson);
    if (getSchedule.isPresent()){
      throw new ResourceAlreadyExistsException("Lesson has already schedule with lesson ID = " + lessonId);
    }

    //Check if the study time is overlapping
    Date startDate = calendarRequestDTO.getStartDateTime();
    Date endDate = calendarRequestDTO.getEndDateTime();
    if (scheduleRepository.findScheduleByLessonAndStartDateAndEndDate(lesson,startDate , endDate).size() > 0){
      throw new InvalidValueException("This time has a schedule");
    }
    schedule.setLesson(lesson);
    schedule.setTitle(calendarRequestDTO.getTitle());

    //Create event on gg calendar
    Event event = new Event()
        .setSummary(calendarRequestDTO.getTitle())
        .setLocation("Ho_Chi_Minh")
        .setDescription("Hangouts meet");

    EventDateTime start = new EventDateTime()
        .setDateTime(new DateTime(calendarRequestDTO.getStartDateTime()))
        .setTimeZone("Asia/Ho_Chi_Minh");
    event.setStart(start);


    schedule.setStartDate(calendarRequestDTO.getStartDateTime());

    // DateTime endDateTime = new DateTime("2020-05-05T12:00:00+06:00");//"2020-05-05T12:00:00+06:00");
    EventDateTime end = new EventDateTime()
        .setDateTime(new DateTime(calendarRequestDTO.getEndDateTime()))
        .setTimeZone("Asia/Ho_Chi_Minh");
    event.setEnd(end);

    String[] recurrence = new String[]{"RRULE:FREQ=DAILY;COUNT=1"};
    event.setRecurrence(Arrays.asList(recurrence));


    schedule.setEndDate(calendarRequestDTO.getEndDateTime());

    //Get student of course
    List<StudentCourse> studentCourseList = studentCourseRepository.findStudentCoursesByCourse(
        lesson.getCourse());
    EventAttendee[] attendees = new EventAttendee[studentCourseList.size()];
    for (int i = 0; i < studentCourseList.size(); i++) {
      attendees[i] = new EventAttendee().setEmail(studentCourseList.get(i).getUser().getEmail());
    }

    event.setAttendees(Arrays.asList(attendees));

    EventReminder[] reminderOverrides = new EventReminder[]{
        new EventReminder().setMethod("email").setMinutes(24 * 60),
        new EventReminder().setMethod("popup").setMinutes(10),
    };

    Event.Reminders reminders = new Event.Reminders()
        .setUseDefault(false)
        .setOverrides(Arrays.asList(reminderOverrides));
    event.setReminders(reminders);

    //Set infor conference
    ConferenceSolutionKey conferenceSKey = new ConferenceSolutionKey();
    conferenceSKey.setType("hangoutsMeet"); // Non-G suite user
    CreateConferenceRequest createConferenceReq = new CreateConferenceRequest();
    createConferenceReq.setRequestId(UUID.randomUUID().toString()); // ID generated by you
    createConferenceReq.setConferenceSolutionKey(conferenceSKey);
    ConferenceData conferenceData = new ConferenceData();
    conferenceData.setCreateRequest(createConferenceReq);
    event.setConferenceData(conferenceData);

    String calendarId = "primary";

    try {
      event = client.events().insert(calendarId, event).setConferenceDataVersion(1).execute();
    } catch (IOException e) {
      e.printStackTrace();
    }

    schedule.setMeetId(event.getId());
    schedule.setLink(event.getHangoutLink());

    ScheduleResponseDTO scheduleResponseDTO = ScheduleMapper.INSTANCE.scheduleToScheduleResponseDTO(scheduleRepository.save(schedule));

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Hangout link.", scheduleResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> updateSchedule(CalendarRequestDTO calendarRequestDTO,
      Long id, String code, GoogleAuthorizationCodeFlow flow, GoogleClientSecrets clientSecrets, Calendar client)
      throws IOException, GeneralSecurityException {
    Schedule schedule = scheduleRepository.findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException("Could not find schedule with ID = " + id));

    //Check if the study time is overlapping
    Date startDate = calendarRequestDTO.getStartDateTime();
    Date endDate = calendarRequestDTO.getEndDateTime();
    if (scheduleRepository.findScheduleByLessonAndStartDateAndEndDate(schedule.getLesson(),startDate , endDate).size() > 0){
      throw new InvalidValueException("This time has a schedule");
    }

    // Retrieve the event from the API
    Event event = client.events().get("primary", schedule.getMeetId()).execute();

    EventDateTime start = new EventDateTime()
        .setDateTime(new DateTime(calendarRequestDTO.getStartDateTime()))
        .setTimeZone("Asia/Ho_Chi_Minh");
    event.setStart(start);

    schedule.setStartDate(calendarRequestDTO.getStartDateTime());

    // DateTime endDateTime = new DateTime("2020-05-05T12:00:00+06:00");//"2020-05-05T12:00:00+06:00");
    EventDateTime end = new EventDateTime()
        .setDateTime(new DateTime(calendarRequestDTO.getEndDateTime()))
        .setTimeZone("Asia/Ho_Chi_Minh");
    event.setEnd(end);

    schedule.setEndDate(calendarRequestDTO.getEndDateTime());

    //Update title
    if (calendarRequestDTO.getTitle() != null){
      schedule.setTitle(calendarRequestDTO.getTitle());
      event.setSummary(calendarRequestDTO.getTitle());
    }

    //Update event on calendar
    client.events().update("primary", schedule.getMeetId(), event).execute();
    ScheduleResponseDTO scheduleResponseDTO = ScheduleMapper.INSTANCE.scheduleToScheduleResponseDTO(scheduleRepository.save(schedule));
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Update schedule success!", scheduleResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> deleteSchedule(Long id, String code, GoogleAuthorizationCodeFlow flow,
      GoogleClientSecrets clientSecrets, Calendar client) throws IOException, GeneralSecurityException {
    Schedule schedule = scheduleRepository.findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException("Could not find schedule with ID = " + id));

    //Delete event on calendar
    client.events().delete("primary", schedule.getMeetId()).execute();

    scheduleRepository.delete(schedule);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Delete schedule success!"));
  }

  @Override
  public ResponseEntity<ResponseObject> getScheduleByCourse(Long courseId) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

    List<Lesson> lessonList = lessonRepository.findLessonsByCourse(course);

    //Convert schedule to ScheduleResponseDTO
    List<ScheduleResponseDTO> scheduleResponseDTOS = new ArrayList<>();
    for (Lesson lesson : lessonList) {
      Schedule schedule = scheduleRepository.findScheduleByLesson(lesson)
          .orElseThrow(() -> new ResourceNotFoundException("Could not find schedule with lesson ID = " + lesson.getId()));
      ScheduleResponseDTO scheduleResponseDTO = ScheduleMapper.INSTANCE.scheduleToScheduleResponseDTO(schedule);
      scheduleResponseDTOS.add(scheduleResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List schedule.", scheduleResponseDTOS));
  }

  @Override
  public ResponseEntity<?> getScheduleByLesson(Long lessonId) {
    Lesson lesson = lessonRepository.findById(lessonId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find lesson with ID = " + lessonId));

    Schedule schedule = scheduleRepository.findScheduleByLesson(lesson)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find schedule with lesson ID = " + lessonId));

    ScheduleResponseDTO scheduleResponseDTO = ScheduleMapper.INSTANCE.scheduleToScheduleResponseDTO(schedule);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List Schedule.", scheduleResponseDTO));
  }

  @Override
  public ResponseEntity<?> getScheduleByTeacher(Long teacherId) {
    User teacher = userRepository.findById(teacherId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find teacher with ID = " + teacherId));

    List<Course> courseList = courseRepository.findCoursesByTeacher(teacher);
    List<ScheduleResponseDTO> scheduleResponseDTOS = new ArrayList<>();
    for (Course course : courseList){
      //Get list lesson by course
      List<Lesson> lessonList = lessonRepository.findLessonsByCourse(course);

      for (Lesson lesson : lessonList) {
        Optional<Schedule> schedule = scheduleRepository.findScheduleByLesson(lesson);
        if (schedule.isPresent()){
          ScheduleResponseDTO scheduleResponseDTO = ScheduleMapper.INSTANCE.scheduleToScheduleResponseDTO(schedule.get());
          scheduleResponseDTOS.add(scheduleResponseDTO);
        }
      }
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List schedule.", scheduleResponseDTOS));
  }

  @Override
  public ResponseEntity<?> getScheduleById(Long id) {
    Schedule schedule = scheduleRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find schedule with ID = " + id));
    ScheduleResponseDTO scheduleResponseDTO = ScheduleMapper.INSTANCE.scheduleToScheduleResponseDTO(schedule);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Schedule by ID = " + id, scheduleResponseDTO));
  }
}
