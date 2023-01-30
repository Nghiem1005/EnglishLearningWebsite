package com.example.englishlearningwebsite.service.implement;

import com.example.englishlearningwebsite.dto.response.CourseResponseDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.dto.response.StatisticByDayResponseDTO;
import com.example.englishlearningwebsite.dto.response.StatisticInfoCourseResponseDTO;
import com.example.englishlearningwebsite.dto.response.StatisticResponseDTO;
import com.example.englishlearningwebsite.entities.Course;
import com.example.englishlearningwebsite.entities.Lesson;
import com.example.englishlearningwebsite.entities.User;
import com.example.englishlearningwebsite.entities.enums.CourseType;
import com.example.englishlearningwebsite.exception.ResourceNotFoundException;
import com.example.englishlearningwebsite.mapper.CourseMapper;
import com.example.englishlearningwebsite.models.ICourseSeller;
import com.example.englishlearningwebsite.models.IStatisticDay;
import com.example.englishlearningwebsite.repository.BillRepository;
import com.example.englishlearningwebsite.repository.CourseRepository;
import com.example.englishlearningwebsite.repository.ExerciseRepository;
import com.example.englishlearningwebsite.repository.LessonRepository;
import com.example.englishlearningwebsite.repository.RoleRepository;
import com.example.englishlearningwebsite.repository.StudentCourseRepository;
import com.example.englishlearningwebsite.repository.TestRepository;
import com.example.englishlearningwebsite.repository.UserRepository;
import com.example.englishlearningwebsite.service.StatisticService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StatisticServiceImpl implements StatisticService {
  @Autowired private CourseRepository courseRepository;
  @Autowired private StudentCourseRepository studentCourseRepository;
  @Autowired private TestRepository testRepository;
  @Autowired private LessonRepository lessonRepository;
  @Autowired private ExerciseRepository exerciseRepository;
  @Autowired private BillRepository billRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private RoleRepository roleRepository;
  @Override
  public ResponseEntity<?> statisticInfoCourse(Long courseId) {
    Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

    StatisticInfoCourseResponseDTO info = new StatisticInfoCourseResponseDTO();
    info.setDocumentAmount(course.getDocument().size());
    info.setStudentAmount(studentCourseRepository.findStudentCoursesByCourse(course).size());
    info.setTestAmount(testRepository.findTestsByCourse(course).size());
    info.setVideoAmount(lessonRepository.findLessonsByCourse(course).size());

    //Get amount exercise
    int exerciseAmount = 0;
    for (Lesson lesson : lessonRepository.findLessonsByCourse(course)) {
      exerciseAmount = exerciseAmount + exerciseRepository.findExercisesByLesson(lesson).size();
    }
    info.setExerciseAmount(exerciseAmount);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Info", info));
  }

  @Override
  public ResponseEntity<?> statisticBestSeller() {
    List<ICourseSeller> courseSellerList = billRepository.bestSeller();
    List<CourseResponseDTO> courseResponseDTOList = new ArrayList<>();
    for (ICourseSeller iCourseSeller : courseSellerList) {
      Course course = courseRepository.findById(iCourseSeller.getCourseId()).orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + iCourseSeller.getCourseId()));
      CourseResponseDTO courseResponseDTO = CourseMapper.INSTANCE.courseToCourseResponseDTO(course);
      courseResponseDTOList.add(courseResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Best seller.", courseResponseDTOList));
  }

  @Override
  public ResponseEntity<?> generalStatistics() {
    List<User> userList = userRepository.findUsersByRole(roleRepository.findById(Long.parseLong("3")).get());
    List<Course> courseList = courseRepository.findAll();
    List<User> teacherList = userRepository.findUsersByRole(roleRepository.findById(Long.parseLong("2")).get());
    double revenue = billRepository.totalPrice();

    StatisticResponseDTO statisticResponseDTO = new StatisticResponseDTO(userList.size(), revenue, courseList.size(), teacherList.size());
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "General statistic.", statisticResponseDTO));
  }

  @Override
  public ResponseEntity<?> statisticsByDay() {
    //Get revenue in 7 days
    List<IStatisticDay> revenueStatistic = billRepository.findRevenueByDay(7);
    //Get student attended course video in 7 days
    List<IStatisticDay> statisticStudentCourseVideoList = studentCourseRepository.findAllStudentTypeByDay(7,
        CourseType.SELF_LEARNING.name());
    //Get student attended course with teacher in 7 days
    List<IStatisticDay> statisticStudentCourseTeacherList = studentCourseRepository.findAllStudentTypeByDay(7,
        CourseType.WITH_TEACHER.name());
    //Get revenue course video in 7 days
    List<IStatisticDay> revenueStatisticCourseVideo = billRepository.findRevenueByDayAndTypeCourse(7,
        CourseType.SELF_LEARNING.name());
    //Get revenue course with teacher in 7 days
    List<IStatisticDay> revenueStatisticCourseTeacher = billRepository.findRevenueByDayAndTypeCourse(7,
        CourseType.WITH_TEACHER.name());
    //Get new member in 7 days
    List<IStatisticDay> newMemberStatistic = userRepository.findAllNewMemberByDay(7);

    String[] daysOfWeek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    List<StatisticByDayResponseDTO> statisticByDayResponseDTOS = new ArrayList<>();

    //Get get list of last 7 days from recent to more distant
    List<String> dayList = new ArrayList<>();
    for (int i=0; i < 7; i++){
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.DAY_OF_WEEK, - i);
      int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
      if (dayOfWeek == 1){
        dayList.add("Sun");
      } else {
        dayList.add(daysOfWeek[dayOfWeek - 2]);
      }
    }

    //Set statistics by corresponding date
    for (String day : dayList){
      StatisticByDayResponseDTO statisticByDayResponseDTO = new StatisticByDayResponseDTO();
      statisticByDayResponseDTO.setNewMember(0);
      statisticByDayResponseDTO.setStudentCourseWithVideo(0);
      statisticByDayResponseDTO.setStudentCourseWithTeacher(0);
      statisticByDayResponseDTO.setRevenueTotal(0);
      statisticByDayResponseDTO.setRevenueCourseTeacher(0);
      statisticByDayResponseDTO.setRevenueCourseVideo(0);

      statisticByDayResponseDTO.setTime(day);

      //Set amount new member
      for (IStatisticDay statistic : newMemberStatistic) {
        if (daysOfWeek[statistic.getWeekDay()].equals(day)){
          statisticByDayResponseDTO.setNewMember((int) statistic.getTotalValue());
        }
      }

      //Set amount student attended course video
      for (IStatisticDay statistic : statisticStudentCourseVideoList) {
        if (daysOfWeek[statistic.getWeekDay()].equals(day)){
          statisticByDayResponseDTO.setStudentCourseWithVideo((int) statistic.getTotalValue());
        }
      }

      //Set amount student attended course with teacher
      for (IStatisticDay statistic : statisticStudentCourseTeacherList) {
        if (daysOfWeek[statistic.getWeekDay()].equals(day)){
          statisticByDayResponseDTO.setStudentCourseWithTeacher((int) statistic.getTotalValue());
        }
      }

      //Set revenue
      for (IStatisticDay statistic : revenueStatistic) {
        if (daysOfWeek[statistic.getWeekDay()].equals(day)){
          statisticByDayResponseDTO.setRevenueTotal(statistic.getTotalValue());
        }
      }

      //Set revenue course video
      for (IStatisticDay statistic : revenueStatisticCourseVideo) {
        if (daysOfWeek[statistic.getWeekDay()].equals(day)){
          statisticByDayResponseDTO.setRevenueCourseVideo(statistic.getTotalValue());
        }
      }

      //Set revenue course with teacher
      for (IStatisticDay statistic : revenueStatisticCourseTeacher) {
        if (daysOfWeek[statistic.getWeekDay()].equals(day)){
          statisticByDayResponseDTO.setRevenueCourseTeacher(statistic.getTotalValue());
        }
      }
      statisticByDayResponseDTOS.add(statisticByDayResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Statistic by day.", statisticByDayResponseDTOS));
  }
}
