package com.edf.demo.exception;

public class EdfGeneralException extends RuntimeException {
  public EdfGeneralException(Exception e) {
    super("Something went wrong", e);
  }
}
