package com.edf.demo.exception;

public class EdfBadRequestException extends RuntimeException {
  public EdfBadRequestException(String message) {
    super(message);
  }
}
