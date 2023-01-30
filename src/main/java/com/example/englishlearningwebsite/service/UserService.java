package com.example.englishlearningwebsite.service;

import com.example.englishlearningwebsite.dto.request.UserRequestDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.dto.response.UserResponseDTO;
import com.example.englishlearningwebsite.entities.User;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface UserService {
  ResponseEntity<?> saveUser(UserRequestDTO userRequestDTO, String siteUrl)
      throws MessagingException, UnsupportedEncodingException;
  ResponseEntity<ResponseObject> verifyUser(String verifyCode);
  ResponseEntity<ResponseObject> updateUser(Long id, UserRequestDTO userRequestDTO);
  ResponseEntity<?> getAllUser(Pageable pageable);
  ResponseEntity<?> getAllUserOutCourse();
  ResponseEntity<?> getAllTeacher(Pageable pageable);
  ResponseEntity<ResponseObject> deleteUser(Long id);
  ResponseEntity<ResponseObject> getUserById(Long id);
  User processOAuthPostLogin(User user);
  ResponseEntity<ResponseObject> updatePassword(Long userId, String newPassword, String oldPassword);
}
