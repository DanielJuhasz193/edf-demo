package com.edf.demo.exception;

public class EdfNotFoundException extends RuntimeException {
  public EdfNotFoundException() {
    super("Resource not found");
  }
}
