package com.example.englishlearningwebsite.dto.response;

import com.example.englishlearningwebsite.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
  private Long id;
  private String name;
  private String email;
  private String phone;
  private boolean enable;
  private String image;
  private Role role;
  private String provider;
}
