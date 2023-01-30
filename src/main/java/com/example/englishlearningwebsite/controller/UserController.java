package com.example.englishlearningwebsite.controller;

import com.example.englishlearningwebsite.dto.request.UserRequestDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.dto.response.UserResponseDTO;
import com.example.englishlearningwebsite.service.StudentCourseService;
import com.example.englishlearningwebsite.service.UserService;
import com.example.englishlearningwebsite.utils.Utils;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController {
  @Autowired
  private UserService userService;

  @Autowired private StudentCourseService studentCourseService;

  @PutMapping(value = "/{id}")
  public ResponseEntity<ResponseObject> updateUser(@PathVariable(name = "id") Long id, @ModelAttribute @Valid UserRequestDTO userRequestDTO){
    return userService.updateUser(id, userRequestDTO);
  }

  @PutMapping(value = "/password/{userId}")
  public ResponseEntity<ResponseObject> updateUser(@RequestPart(name = "newPassword") String newPassword,
      @PathVariable(name = "userId") Long userId, @RequestPart(name = "oldPassword") String oldPassword){
    return userService.updatePassword(userId, newPassword, oldPassword);
  }

  @GetMapping(value = "")
  public ResponseEntity<?> getAllUser(@RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page){
    Pageable pageable = PageRequest.of(page - 1, size);
    return userService.getAllUser(pageable);
  }

  @GetMapping(value = "/teacher")
  public ResponseEntity<?> getAllTeacher(@RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page){
    Pageable pageable = PageRequest.of(page - 1, size);
    return userService.getAllTeacher(pageable);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<ResponseObject> deleteUser(@PathVariable(name = "id") Long id){
    return userService.deleteUser(id);
  }

  @PostMapping(value = "")
  public ResponseEntity<?> createUser(@ModelAttribute @Valid UserRequestDTO userRequestDTO)
      throws MessagingException, UnsupportedEncodingException {
    return userService.saveUser(userRequestDTO, "");
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<ResponseObject> getUserById(@PathVariable(name = "id") Long id){
    return userService.getUserById(id);
  }

  @GetMapping(value = "/student/course")
  public ResponseEntity<?> getStudentByCourse(@RequestParam(name = "courseId") Long courseId,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return studentCourseService.getStudentByCourse(pageable, courseId);
  }

  @GetMapping(value = "/student/course/null")
  public ResponseEntity<?> getStudentByCourse() {
    return userService.getAllUserOutCourse();
  }
}
