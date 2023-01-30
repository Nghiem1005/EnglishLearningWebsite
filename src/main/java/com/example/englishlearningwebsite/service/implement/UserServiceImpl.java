package com.example.englishlearningwebsite.service.implement;

import com.example.englishlearningwebsite.dto.request.UserRequestDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.dto.response.UserResponseDTO;
import com.example.englishlearningwebsite.entities.Course;
import com.example.englishlearningwebsite.entities.Feedback;
import com.example.englishlearningwebsite.entities.Role;
import com.example.englishlearningwebsite.entities.StudentCourse;
import com.example.englishlearningwebsite.entities.User;
import com.example.englishlearningwebsite.entities.enums.AuthProvider;
import com.example.englishlearningwebsite.exception.ResourceAlreadyExistsException;
import com.example.englishlearningwebsite.exception.ResourceNotFoundException;
import com.example.englishlearningwebsite.mapper.UserMapper;
import com.example.englishlearningwebsite.repository.CourseRepository;
import com.example.englishlearningwebsite.repository.FeedbackRepository;
import com.example.englishlearningwebsite.repository.RoleRepository;
import com.example.englishlearningwebsite.repository.StudentCourseRepository;
import com.example.englishlearningwebsite.repository.UserRepository;
import com.example.englishlearningwebsite.service.ImageStorageService;
import com.example.englishlearningwebsite.service.UserService;
import com.example.englishlearningwebsite.utils.Utils;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import net.bytebuddy.utility.RandomString;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private RoleRepository roleRepository;
  @Autowired
  private JavaMailSender mailSender;

  @Autowired
  private ImageStorageService imageStorageService;

  @Autowired
  private StudentCourseRepository studentCourseRepository;

  @Autowired
  private FeedbackRepository feedbackRepository;
  @Autowired
  private CourseRepository courseRepository;

  @Override
  public ResponseEntity<?> saveUser(UserRequestDTO userRequestDTO, String siteUrl)
      throws MessagingException, UnsupportedEncodingException {
    User user = UserMapper.INSTANCE.userRequestDTOtoUser(userRequestDTO);

    //Check phone user existed
    Optional<User> userCheckPhone = userRepository.findUserByPhone(userRequestDTO.getPhone());
    if (userCheckPhone.isPresent()) {
      throw new ResourceAlreadyExistsException("Phone user existed");
    }

    //Check email user existed
    Optional<User> userCheckEmail = userRepository.findUserByEmail(userRequestDTO.getEmail());
    if (userCheckEmail.isPresent()) {
      throw new ResourceAlreadyExistsException("Email user existed");
    }

    encodePassword(user);
    //Check role already exists
    Role role = roleRepository.findById(user.getRole().getId()).orElseThrow(
        () -> new ResourceNotFoundException(
            "Could not find role with ID = " + user.getRole().getId()));
    user.setRole(role);
    user.setEnable(true);

    //Save image
    if (userRequestDTO.getImage() != null) {
      String nameFile = imageStorageService.storeFile(userRequestDTO.getImage(),
          Utils.IMAGE_UPLOAD_USER_URI);
      user.setImages(nameFile);
    }

    user.setProvider(AuthProvider.local);

    String randomCodeVerify = RandomString.make(64);
    user.setVerificationCode(randomCodeVerify);

    if (siteUrl != null) {
      sendVerificationEmail(user, siteUrl);
    }

    UserResponseDTO userResponseDTO = UserMapper.INSTANCE.userToUserResponseDTO(
        userRepository.save(user));

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Create user successfully!", userResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> verifyUser(String verifyCode) {
    User getUser = userRepository.findUserByVerificationCode(verifyCode)
        .orElseThrow(() -> new ResourceNotFoundException("Verify code is incorrect"));
    getUser.setEnable(true);
    User user = userRepository.save(getUser);
    UserResponseDTO userResponseDTO = UserMapper.INSTANCE.userToUserResponseDTO(
        userRepository.save(user));
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Verify account success!!!", userResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> updateUser(Long id, UserRequestDTO userRequestDTO) {
    //User user = UserMapper.INSTANCE.userRequestDTOtoUser(userRequestDTO);
    //user.setId(id);
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + id));

    //Check email user existed
    if (userRequestDTO.getEmail() != null){
      if (!userRequestDTO.getEmail().equals(user.getEmail())){
        Optional<User> userCheckEmail = userRepository.findUserByEmail(userRequestDTO.getEmail());
        if (userCheckEmail.isPresent()) {
          if (userCheckEmail.get().getId() != user.getId()) {
            throw new ResourceAlreadyExistsException("Email user existed");
          }
        }
      }
    }


    //Check phone user existed
    if (userRequestDTO.getPhone() != null){
      if (!userRequestDTO.getPhone().equals(user.getPhone())){
        Optional<User> userCheckPhone = userRepository.findUserByPhone(userRequestDTO.getPhone());
        if (userCheckPhone.isPresent()) {
          if (userCheckPhone.get().getId() != user.getId()) {
            throw new ResourceAlreadyExistsException("Phone user existed");
          }
        }
      }
    }

    //Storage image
    if (userRequestDTO.getImage() != null) {
      user.setImages(
          imageStorageService.storeFile(userRequestDTO.getImage(), Utils.IMAGE_UPLOAD_USER_URI));
    }

    if (userRequestDTO.getPassword() != null) {
      user.setPassword(userRequestDTO.getPassword());
      encodePassword(user);
    }

    //Set enable
    user.setEnable(userRequestDTO.isEnable());

    //Set name
    if (userRequestDTO.getName() != null){
      user.setName(userRequestDTO.getName());

    }
    //encodePassword(user);
    //Check role already exists
    Role role = roleRepository.findById(user.getRole().getId()).orElseThrow(
        () -> new ResourceNotFoundException(
            "Could not find role with ID = " + user.getRole().getId()));
    user.setRole(role);
    UserResponseDTO userResponseDTO = UserMapper.INSTANCE.userToUserResponseDTO(
        userRepository.save(user));

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Update user successfully!",
            userResponseDTO));
  }

  @Override
  public ResponseEntity<?> getAllUser(Pageable pageable) {
    List<UserResponseDTO> userResponseDTOList = new ArrayList<>();
    Page<User> getUserList = userRepository.findAll(pageable);
    List<User> userList = getUserList.getContent();
    for (User user : userList) {
      UserResponseDTO userResponseDTO = UserMapper.INSTANCE.userToUserResponseDTO(user);
      userResponseDTOList.add(userResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Success", userResponseDTOList));
  }

  @Override
  public ResponseEntity<?> getAllUserOutCourse() {
    List<UserResponseDTO> userResponseDTOList = new ArrayList<>();
    List<User> userList = userRepository.findUsersOutCourse();
    for (User user : userList) {
      UserResponseDTO userResponseDTO = UserMapper.INSTANCE.userToUserResponseDTO(user);
      userResponseDTOList.add(userResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Success", userResponseDTOList));
  }

  @Override
  public ResponseEntity<?> getAllTeacher(Pageable pageable) {
    List<UserResponseDTO> userResponseDTOList = new ArrayList<>();
    Role role = roleRepository.findById(Long.parseLong("2"))
        .orElseThrow(() -> new ResourceNotFoundException("Could not find role teacher"));
    Page<User> getUserList = userRepository.findUsersByRole(role, pageable);

    //Convert user to UserResponseDTO
    List<User> userList = getUserList.getContent();
    for (User user : userList) {
      UserResponseDTO userResponseDTO = UserMapper.INSTANCE.userToUserResponseDTO(user);
      userResponseDTOList.add(userResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Success", userResponseDTOList));
  }

  @Override
  public ResponseEntity<ResponseObject> deleteUser(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + id));

    if (user.getRole().getId() == 2) {
      //Update teacher of course to null
      List<Course> courseList = courseRepository.findCoursesByTeacher(user);
      for (Course course : courseList) {
        course.setTeacher(null);
        courseRepository.save(course);
      }
    } else {
      //Delete course of student
      List<StudentCourse> studentCourseList = studentCourseRepository.findStudentCoursesByUser(
          user);
      studentCourseRepository.deleteAll(studentCourseList);
    }

    //Delete feedback by student
    List<Feedback> feedbackList = feedbackRepository.findFeedbacksByUser(user);
    feedbackRepository.deleteAll(feedbackList);

    imageStorageService.deleteFile(user.getImages(), "user");
    userRepository.delete(user);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Delete user success!!!", null));
  }

  @Override
  public ResponseEntity<ResponseObject> getUserById(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + id));
    UserResponseDTO userResponseDTO = UserMapper.INSTANCE.userToUserResponseDTO(user);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Success", userResponseDTO));
  }

  @Override
  public User processOAuthPostLogin(User user) {
    Optional<User> existUser = userRepository.findUserByName(user.getName());

    if (!existUser.isPresent()) {
      User newUser = new User();
      newUser.setName(user.getName());
      newUser.setEnable(true);
      newUser.setPassword(user.getPassword());
      newUser.setEmail(user.getEmail());
      newUser.setRole(user.getRole());

      return userRepository.save(newUser);
    }
    return existUser.get();
  }

  @Override
  public ResponseEntity<ResponseObject> updatePassword(Long userId, String newPassword, String oldPassword) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + userId));

    //Check password
    String password = passwordEncoder.encode(oldPassword);
    if (!passwordEncoder.matches(oldPassword, user.getPassword())){
      throw new ResourceNotFoundException("Mật khẩu tài khoản không chính xác");
    }

    //Update new password
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Update password success"));
  }

  private void encodePassword(User user) {
    String encodedPassword = passwordEncoder.encode(user.getPassword());
    user.setPassword(encodedPassword);
  }

  private void sendVerificationEmail(User user, String siteUrl)
      throws MessagingException, UnsupportedEncodingException {
    //Design mail form
    String subject = "Please verify your registration";
    String senderName = "Mobile University Store";
    String verifyUrl = siteUrl + "/verify?code=" + user.getVerificationCode();
    String mailContent = "<p>Dear " + user.getName() + ",<p><br>"
        + "Please click the link below to verify your registration:<br>"
        + "<h3><a href = \"" + verifyUrl + "\">VERIFY</a></h3>"
        + "Thank you,<br>" + "Mobile University.";

    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper messageHelper = new MimeMessageHelper(message);

    messageHelper.setFrom("mobileuniversity@gmail.com", senderName);
    messageHelper.setTo(user.getEmail());
    messageHelper.setSubject(subject);
    messageHelper.setText(mailContent, true);
    mailSender.send(message);
  }
}
