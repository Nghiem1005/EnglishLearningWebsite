package com.example.englishlearningwebsite.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
  private String name;
  @Size(message="Invalid size.", max = 30, min=10)
  @Pattern(regexp=("^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@"
      + "[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$"), message = "Invalid email")
  private String email;
  private String phone;
  private String password;
  private boolean enable;
  private Long role;
  MultipartFile image;
}
