package com.edf.demo.api;

import com.edf.demo.api.dto.ErrorResponse;
import com.edf.demo.exception.EdfBadRequestException;
import com.edf.demo.exception.EdfGeneralException;
import com.edf.demo.exception.EdfNotFoundException;
import com.edf.demo.exception.EdfStateException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {

  @ExceptionHandler(EdfBadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleBadRequest(EdfBadRequestException ex) {
    return new ResponseEntity<>(getBody(ex.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(EdfNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ErrorResponse> handleNotFound(EdfNotFoundException ex) {
    return new ResponseEntity<>(getBody(ex.getMessage()), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(EdfGeneralException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ErrorResponse> handleGeneral(EdfGeneralException ex) {
    log.error("Unexpected error during request", ex);
    return new ResponseEntity<>(getBody(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(EdfStateException.class)
  @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
  public ResponseEntity<ErrorResponse> handleWrongState(EdfStateException ex) {
    return new ResponseEntity<>(getBody(ex.getMessage()), HttpStatus.PRECONDITION_FAILED);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleValidation(ConstraintViolationException ex) {
    var message =
        ex.getConstraintViolations().stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.joining(", "));
    return new ResponseEntity<>(getBody(message), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleValidation(
      MissingServletRequestParameterException ex) {
    return new ResponseEntity<>(
        getBody(ex.getParameterName() + " must be provided"), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ErrorResponse> handleException(Exception ex) {
    log.error("Unexpected error during request", ex);
    return new ResponseEntity<>(getBody("Something went wrong"), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private ErrorResponse getBody(String message) {
    return ErrorResponse.builder().message(message).build();
  }
}
