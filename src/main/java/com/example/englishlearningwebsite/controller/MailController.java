package com.example.englishlearningwebsite.controller;


import com.example.englishlearningwebsite.dto.request.MailRequestDTO;
import com.example.englishlearningwebsite.dto.request.UserRequestDTO;
import com.example.englishlearningwebsite.service.MailService;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailController {
  @Autowired private MailService mailService;
  @PostMapping(value = "/sendMail")
  public void sendFormRegister(@RequestBody MailRequestDTO mailRequestDTO, HttpServletRequest request)
      throws MessagingException, UnsupportedEncodingException {
    String siteUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
    mailService.sendFormRegister(siteUrl, mailRequestDTO);
  }

  @PostMapping(value = "/newPassword")
  public void sendNewPassword(@RequestBody MailRequestDTO mailRequestDTO, HttpServletRequest request)
      throws MessagingException, UnsupportedEncodingException {
    String siteUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
    mailService.sendNewPassword(siteUrl, mailRequestDTO);
  }
}
