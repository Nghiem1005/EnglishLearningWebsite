package com.example.englishlearningwebsite.service.implement;

import com.example.englishlearningwebsite.dto.request.MailRequestDTO;
import com.example.englishlearningwebsite.entities.User;
import com.example.englishlearningwebsite.exception.ResourceNotFoundException;
import com.example.englishlearningwebsite.repository.UserRepository;
import com.example.englishlearningwebsite.service.MailService;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
  @Autowired
  private JavaMailSender mailSender;
  @Autowired
  private UserRepository userRepository;
  @Autowired private PasswordEncoder passwordEncoder;
  private final String LINK_FORM = "http://localhost:3000/form-register";

  @Override
  public void sendFormRegister(String siteUrl, MailRequestDTO mailRequestDTO)
      throws MessagingException, UnsupportedEncodingException {
    //Design form mail
    String subject = "Please click link to registration";
    String senderName = "Captain Britain";
    String verifyUrl = LINK_FORM;
    String mailContent = "Please click the link below to redirect to form register:<br>"
        + "<h3><a href = \"" + verifyUrl + "\">FORM REGISTER</a></h3>"
        + "Thank you,<br>" + mailRequestDTO.getName() + ".";

    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper messageHelper = new MimeMessageHelper(message);

    messageHelper.setFrom("CaptainBritain@gmail.com", senderName);
    messageHelper.setTo(mailRequestDTO.getEmail());
    messageHelper.setSubject(subject);
    messageHelper.setText(mailContent, true);
    mailSender.send(message);
  }

  @Override
  public void sendNewPassword(String siteUrl, MailRequestDTO mailRequestDTO)
      throws MessagingException, UnsupportedEncodingException {
    User user = userRepository.findUserByEmail(mailRequestDTO.getEmail()).orElseThrow(() -> new ResourceNotFoundException("Could not find user with email = " + mailRequestDTO.getEmail()));

    //Design form mail
    String newPassword = UUID.randomUUID().toString().replace("-", "");
    String subject = "Your new password";
    String senderName = "Captain Britain";
    String mailContent = "Your new password is:<br>"
        + "<h3>" + newPassword + "</h3>"
        + "Thank you.";

    String encodedPassword = passwordEncoder.encode(newPassword);
    user.setPassword(encodedPassword);
    userRepository.save(user);

    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper messageHelper = new MimeMessageHelper(message);

    messageHelper.setFrom("CaptainBritain@gmail.com", senderName);
    messageHelper.setTo(mailRequestDTO.getEmail());
    messageHelper.setSubject(subject);
    messageHelper.setText(mailContent, true);
    mailSender.send(message);
  }
}
