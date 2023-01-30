package com.example.englishlearningwebsite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseObject {
  private HttpStatus status;
  private String message;
  private Object data;

  private int totalPage;

  public ResponseObject(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
    this.data = null;
  }

  public ResponseObject(HttpStatus status, String message, Object data) {
    this.status = status;
    this.message = message;
    this.data = data;
    this.totalPage = 0;
  }
}
