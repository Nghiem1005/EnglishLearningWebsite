package com.example.englishlearningwebsite.exception;

public class ResourceNotFoundException extends RuntimeException{

  public ResourceNotFoundException(String message) {
    super(message);
  }
}
