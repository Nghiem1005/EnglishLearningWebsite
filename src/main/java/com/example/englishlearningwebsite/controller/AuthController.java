package com.example.englishlearningwebsite.controller;

import com.example.englishlearningwebsite.dto.request.AuthRequestDTO;
import com.example.englishlearningwebsite.dto.request.RefreshTokenRequestDTO;
import com.example.englishlearningwebsite.dto.request.UserRequestDTO;
import com.example.englishlearningwebsite.dto.response.AuthResponseDTO;
import com.example.englishlearningwebsite.dto.response.RefreshTokenResponseDTO;
import com.example.englishlearningwebsite.dto.response.ResponseObject;
import com.example.englishlearningwebsite.entities.RefreshToken;
import com.example.englishlearningwebsite.entities.User;
import com.example.englishlearningwebsite.entities.principal.UserPrincipal;
import com.example.englishlearningwebsite.exception.BadRequestException;
import com.example.englishlearningwebsite.exception.ResourceNotFoundException;
import com.example.englishlearningwebsite.repository.UserRepository;
import com.example.englishlearningwebsite.service.RefreshTokenService;
import com.example.englishlearningwebsite.service.UserService;
import com.example.englishlearningwebsite.utils.TokenProvider;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
  @Autowired
  private UserService userService;

  @Autowired
  private AuthenticationManager auth;

  @Autowired
  private TokenProvider tokenProvider;

  @Autowired
  private RefreshTokenService refreshTokenService;
  @Autowired private UserRepository userRepository;

  @PostMapping(value = "/auth/login")
  public ResponseEntity<?> getUserByEmailAndPassword(@RequestBody @Valid AuthRequestDTO authRequestDTO) {
    UsernamePasswordAuthenticationToken authenticationToken;

    //Check login with phone or email
    if (authRequestDTO.getEmail() == null){
      authenticationToken = new UsernamePasswordAuthenticationToken(authRequestDTO.getPhone(), authRequestDTO.getPassword());
    } else {
      authenticationToken = new UsernamePasswordAuthenticationToken(authRequestDTO.getEmail(), authRequestDTO.getPassword());
    }
    Authentication authentication = auth.authenticate(authenticationToken);
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
    SimpleGrantedAuthority role = (SimpleGrantedAuthority) userPrincipal.getAuthorities().toArray()[0];

    //Check role login
    if (authRequestDTO.getRole().toUpperCase().equals("ADMIN")){
      if (!role.getAuthority().toUpperCase().equals("ROLE_ADMIN")){
        throw new ResourceNotFoundException("Tài khoản không có quyền truy cập.");
      }
    } else {
      if (role.getAuthority().toUpperCase().equals("ROLE_STUDENT") && authRequestDTO.getRole().toUpperCase().equals("TEACHER")){
        throw new ResourceNotFoundException("Tài khoản không có quyền truy cập.");
      }
    }
    String accessToken = tokenProvider.createToken(authentication);

    RefreshToken refreshToken = refreshTokenService.createRefreshToken(authentication);

    AuthResponseDTO authResponseDTO = new AuthResponseDTO(userPrincipal.getId(), accessToken, refreshToken.getToken());
    return ResponseEntity.ok().body(new ResponseObject(HttpStatus.OK, "Login Successfully", authResponseDTO));
  }

  @PostMapping(value = "/register")
  public ResponseEntity<?> createUser(@ModelAttribute @Valid UserRequestDTO userRequestDTO, HttpServletRequest request)
      throws MessagingException, UnsupportedEncodingException {
    String siteUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
    return userService.saveUser(userRequestDTO, siteUrl);
  }

  @GetMapping(value = "/verify")
  public ResponseEntity<ResponseObject> verifyUser(@RequestParam(name = "code") String verifyCode){
    return userService.verifyUser(verifyCode);
  }

  @PostMapping("/refreshToken")
  public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDTO request) {
    String requestRefreshToken = request.getRefreshToken();

    return refreshTokenService.findByToken(requestRefreshToken)
        .map(refreshTokenService::verifyExpiration)
        .map(RefreshToken::getUser)
        .map(user -> {
          String token = tokenProvider.createTokenFromUserId(user.getId());
          return ResponseEntity.ok(new RefreshTokenResponseDTO(token, requestRefreshToken));
        })
        .orElseThrow(() -> new BadRequestException("Refresh token is not in database!"));
  }
}
