package com.example.englishlearningwebsite.service.implement;

import com.example.englishlearningwebsite.dto.response.CourseResponseDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.dto.response.StudentCourseResponseDTO;
import com.example.englishlearningwebsite.dto.response.UserResponseDTO;
import com.example.englishlearningwebsite.entities.Course;
import com.example.englishlearningwebsite.entities.StudentCourse;
import com.example.englishlearningwebsite.entities.User;
import com.example.englishlearningwebsite.exception.ResourceAlreadyExistsException;
import com.example.englishlearningwebsite.exception.ResourceNotFoundException;
import com.example.englishlearningwebsite.mapper.CourseMapper;
import com.example.englishlearningwebsite.mapper.StudentCourseMapper;
import com.example.englishlearningwebsite.mapper.UserMapper;
import com.example.englishlearningwebsite.repository.CourseRepository;
import com.example.englishlearningwebsite.repository.StudentCourseRepository;
import com.example.englishlearningwebsite.repository.UserRepository;
import com.example.englishlearningwebsite.service.StudentCourseService;
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
public class StudentCourseServiceImpl implements StudentCourseService {
  @Autowired private CourseRepository courseRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private StudentCourseRepository studentCourseRepository;

  @Override
  public ResponseEntity<ResponseObject> createStudentCourse(Long studentId, Long courseId) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

    User student = userRepository.findById(studentId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find student with ID = " + studentId));

    Optional<StudentCourse> getStudentCourse = studentCourseRepository.findStudentCourseByCourseAndUser(course, student);

    if (getStudentCourse.isPresent()){
      throw new ResourceAlreadyExistsException("Student attended this course");
    }

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourse(course);
    studentCourse.setUser(student);
    studentCourse.setProgress(1);

    StudentCourseResponseDTO studentCourseResponseDTO = StudentCourseMapper.INSTANCE
        .studentCourseToStudentCourseResponseDTO(studentCourseRepository.save(studentCourse));

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Add student to course success!", studentCourseResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> deleteStudentCourse(Long studentId, Long courseId) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

    User student = userRepository.findById(studentId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find student with ID = " + studentId));

    StudentCourse getStudentCourse = studentCourseRepository.findStudentCourseByCourseAndUser(course, student)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId + " and student ID = " + studentId));

    studentCourseRepository.delete(getStudentCourse);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Delete student to course success!"));
  }

  @Override
  public ResponseEntity<ResponseObject> getCourseByStudent(Pageable pageable, Long studentId) {
    User student = userRepository.findById(studentId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find student with ID = " + studentId));

    Page<StudentCourse> getStudentCourseList = studentCourseRepository.findStudentCoursesByUser(pageable, student);
    List<StudentCourse> studentCourseList = getStudentCourseList.getContent();

    //Convert StudentCourse to StudentCourseResponseDTO
    List<CourseResponseDTO> courseResponseDTOList = new ArrayList<>();

    for (StudentCourse studentCourse : studentCourseList){
      CourseResponseDTO courseResponseDTO = toCourseResponseDTO(studentCourse.getCourse());
      courseResponseDTOList.add(courseResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List course.", courseResponseDTOList, getStudentCourseList.getTotalPages()));
  }

  @Override
  public ResponseEntity<ResponseObject> getStudentByCourse(Pageable pageable, Long courseId) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

    Page<StudentCourse> getStudentCourseList = studentCourseRepository.findStudentCoursesByCourse(pageable, course);
    List<StudentCourse> studentCourseList = getStudentCourseList.getContent();

    //Convert StudentCourse to StudentCourseResponseDTO
    List<UserResponseDTO> userResponseDTOList = new ArrayList<>();

    for (StudentCourse studentCourse : studentCourseList) {
      UserResponseDTO userResponseDTO = UserMapper.INSTANCE.userToUserResponseDTO(studentCourse.getUser());
      userResponseDTOList.add(userResponseDTO);
    }

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Success", userResponseDTOList));
  }

  @Override
  public ResponseEntity<ResponseObject> updateProgress(Long studentId, Long courseId,
      int progress) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

    User student = userRepository.findById(studentId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find student with ID = " + studentId));

    StudentCourse getStudentCourse = studentCourseRepository.findStudentCourseByCourseAndUser(course, student)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId + " and student ID = " + studentId));
    if (progress <= getStudentCourse.getProgress()){
      throw new ResourceAlreadyExistsException("Student studied or studying this lesson. Progress must greater than progress present");
    }
    getStudentCourse.setProgress(progress);

    StudentCourseResponseDTO studentCourseResponseDTO = StudentCourseMapper.INSTANCE
        .studentCourseToStudentCourseResponseDTO(studentCourseRepository.save(getStudentCourse));

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Delete student to course success!", studentCourseResponseDTO));
  }

  private CourseResponseDTO toCourseResponseDTO(Course course){
    CourseResponseDTO courseResponseDTO = CourseMapper.INSTANCE.courseToCourseResponseDTO(course);
    List<String> documentCourseList = course.getDocument();
    String[] documentCourseArray = new String[documentCourseList.size()];
    for (int i=0; i<documentCourseList.size(); i++){
      documentCourseArray[i] = documentCourseList.get(i);
    }
    courseResponseDTO.setDocument(documentCourseArray);

    return courseResponseDTO;
  }
}
