package com.example.englishlearningwebsite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponseDTO {
  private Long id;
  private String name;
  private String description;
}