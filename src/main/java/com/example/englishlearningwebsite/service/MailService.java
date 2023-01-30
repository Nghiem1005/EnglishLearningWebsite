package com.example.englishlearningwebsite.service;

import com.example.englishlearningwebsite.dto.request.MailRequestDTO;
import com.example.englishlearningwebsite.entities.User;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;

public interface MailService {
  void sendFormRegister(String siteUrl, MailRequestDTO mailRequestDTO)
      throws MessagingException, UnsupportedEncodingException;

  void sendNewPassword(String siteUrl, MailRequestDTO mailRequestDTO)
      throws MessagingException, UnsupportedEncodingException;
}
