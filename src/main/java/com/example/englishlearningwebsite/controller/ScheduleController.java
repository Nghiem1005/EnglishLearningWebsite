package com.example.englishlearningwebsite.controller;


import com.example.englishlearningwebsite.dto.request.CalendarRequestDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.repository.CourseRepository;
import com.example.englishlearningwebsite.repository.StudentCourseRepository;
import com.example.englishlearningwebsite.service.CourseService;
import com.example.englishlearningwebsite.service.LessonService;
import com.example.englishlearningwebsite.service.ScheduleService;
import com.example.englishlearningwebsite.utils.Utils;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class ScheduleController {
  private static final String APPLICATION_NAME = "";
  private static HttpTransport httpTransport;
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  GoogleClientSecrets clientSecrets;
  GoogleAuthorizationCodeFlow flow;

  static Calendar client;

  String view;
  @Value("${google.client.client-id}")
  private String clientId;
  @Value("${google.client.client-secret}")
  private String clientSecret;
  @Value("${google.client.redirectUri}")
  private String redirectURI;

  private final String CALENDAR_VIEW = "http://localhost:3001/schedule";

  @Autowired
  private StudentCourseRepository studentCourseRepository;
  @Autowired private CourseService courseService;

  @Autowired
  private ScheduleService scheduleService;
  @Autowired private LessonService lessonService;

  private Set<Event> events = new HashSet<>();

  @RequestMapping(value = "/login/google", method = RequestMethod.GET)
  public RedirectView googleConnectionStatus(@RequestParam(value = "redirectView") String redirectView, @RequestParam(value = "view") String view) throws Exception {
    //RedirectView redirectView = new RedirectView(authorize());
    //redirectAttributes.addAttribute("view", redirectView);
    this.view = view;
    return new RedirectView(authorize(redirectView));
  }

  @RequestMapping(value = "/calendar", method = RequestMethod.GET, params = "code")
  public RedirectView oauth2Callback(@RequestParam(value = "code") String code, RedirectAttributes attributes)
      throws IOException, GeneralSecurityException {
    //Create object calendar on Google
    attributes.addAttribute("code", code);
    TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectURI).execute();
    Credential credential = flow.createAndStoreCredential(response, "userID");
    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    client = new Calendar.Builder(httpTransport, JSON_FACTORY,
        credential)
        .setApplicationName(APPLICATION_NAME).build();
    String calendar_view = "";
    if (this.view.toUpperCase().equals("LESSON")){
      calendar_view = "http://localhost:3001/lesson";
    } else if (this.view.toUpperCase().equals("COURSE")) {
      calendar_view = "http://localhost:3001/course";
    } else {
      calendar_view = "http://localhost:3001/schedule";
    }
    return new RedirectView(calendar_view);
  }

  @DeleteMapping(value = "/lesson/{id}")
  public ResponseEntity<ResponseObject> deleteLesson(@PathVariable(name = "id") Long id, @RequestParam(name = "code", required = false, defaultValue = "") String code)
          throws GeneralSecurityException, IOException {
    return lessonService.deleteLesson(id, code, flow, client);
  }

  @DeleteMapping(value = "/course/{id}")
  public ResponseEntity<?> deleteCourse(@PathVariable(name = "id") Long id, @RequestParam(name = "code", required = false, defaultValue = "") String code)
          throws GeneralSecurityException, IOException {
    return courseService.deleteCourse(id, code, flow, client);
  }

  @PostMapping(value = "/schedule", params = "code")
  public ResponseEntity<ResponseObject> createSchedule(@RequestParam(value = "code") String code,
      @RequestParam(value = "lessonId") Long lessonId,
      @RequestBody CalendarRequestDTO calendarRequestDTO)
      throws GeneralSecurityException, IOException {
    return scheduleService.createSchedule(lessonId, code, calendarRequestDTO, flow, clientSecrets, client);
  }

  @PutMapping(value = "/schedule", params = "code")
  public ResponseEntity<ResponseObject> updateSchedule(@RequestParam(value = "code") String code,
      @RequestParam(value = "id") Long id,
      @RequestBody CalendarRequestDTO calendarRequestDTO)
      throws GeneralSecurityException, IOException {

    return scheduleService.updateSchedule(calendarRequestDTO, id, code, flow, clientSecrets, client);
  }

  @DeleteMapping(value = "/schedule", params = "code")
  public ResponseEntity<ResponseObject> deleteSchedule(@RequestParam(value = "code") String code,
      @RequestParam(value = "id") Long id)
      throws GeneralSecurityException, IOException {

    return scheduleService.deleteSchedule(id, code, flow, clientSecrets, client);
  }

  @GetMapping(value = "/lesson")
  public ResponseEntity<?> getScheduleByLesson(
      @RequestParam(name = "lessonId") Long lessonId) {
    return scheduleService.getScheduleByLesson(lessonId);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<?> getScheduleById(
      @PathVariable(name = "id") Long id) {
    return scheduleService.getScheduleById(id);
  }

  @GetMapping(value = "/course")
  public ResponseEntity<?> getScheduleByCourse(
      @RequestParam(name = "courseId") Long courseId) {
    return scheduleService.getScheduleByCourse(courseId);
  }

  @GetMapping(value = "/teacher")
  public ResponseEntity<?> getScheduleByTeacher(
      @RequestParam(name = "teacherId") Long teacherId) {
    return scheduleService.getScheduleByTeacher(teacherId);
  }

  private String authorize(String redirectView) throws Exception {
    AuthorizationCodeRequestUrl authorizationUrl;

    //Authorize google to create calendar google
    if (flow == null) {
      Details web = new Details();
      web.setClientId(clientId);
      web.setClientSecret(clientSecret);
      clientSecrets = new GoogleClientSecrets().setWeb(web);
      httpTransport = GoogleNetHttpTransport.newTrustedTransport();
      flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
          Collections.singleton(CalendarScopes.CALENDAR)).build();
    }
    authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectView);
    System.out.println("cal authorizationUrl->" + authorizationUrl);
    return authorizationUrl.build();
  }
}
