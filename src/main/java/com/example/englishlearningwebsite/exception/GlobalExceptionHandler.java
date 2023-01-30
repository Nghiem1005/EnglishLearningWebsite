package com.example.englishlearningwebsite.exception;

import com.example.englishlearningwebsite.dto.response.ResponseObject;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(BindException.class)
  @ResponseBody
  protected ResponseEntity<ResponseObject> handleValidationExceptions(
      BindException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.BAD_REQUEST, errors.toString()));
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ResourceAlreadyExistsException.class)
  @ResponseBody
  protected ResponseEntity<ResponseObject> handleConstraintExceptions(RuntimeException e){
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.BAD_REQUEST, e.getMessage()));
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(InvalidValueException.class)
  @ResponseBody
  protected ResponseEntity<ResponseObject> handleInvalidValueExceptions(
      RuntimeException ex) {
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.BAD_REQUEST, ex.getMessage()));
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseBody
  protected ResponseEntity<ResponseObject> handleResourceNotFoundExceptions(RuntimeException e){
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.BAD_REQUEST, e.getMessage()));
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(FileException.class)
  @ResponseBody
  protected ResponseEntity<ResponseObject> handleFileExceptions(RuntimeException e){
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.BAD_REQUEST, e.getMessage()));
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(OAuth2AuthenticationProcessingException.class)
  @ResponseBody
  protected ResponseEntity<ResponseObject> handleOAuth2AuthenticationProcessingExceptions(RuntimeException e){
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.BAD_REQUEST, e.getMessage()));
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(BadRequestException.class)
  @ResponseBody
  protected ResponseEntity<ResponseObject> handleBadRequestExceptions(RuntimeException e){
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.BAD_REQUEST, e.getMessage()));
  }
}
