package com.example.englishlearningwebsite.service.implement;

import com.example.englishlearningwebsite.entities.User;
import com.example.englishlearningwebsite.entities.principal.UserPrincipal;
import com.example.englishlearningwebsite.exception.ResourceNotFoundException;
import com.example.englishlearningwebsite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {
  @Autowired private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = new User();
    //Load user by phone or email
    if (username.contains("@")) {
      user = userRepository.findUserByEmail(username)
          .orElseThrow(() -> new NotFoundException("User " + username + " not found"));
    } else {
      user = userRepository.findUserByPhone(username)
          .orElseThrow(() -> new NotFoundException("User " + username + " not found"));
    }
    return UserPrincipal.create(user);
  }
  @Transactional
  public UserDetails loadUserById(Long id) {
    User user = userRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("User " + id + " not found")
    );

    return UserPrincipal.create(user);
  }
}
