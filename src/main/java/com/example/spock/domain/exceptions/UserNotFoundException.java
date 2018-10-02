package com.example.spock.domain.exceptions;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String userId) {
    super("No user with ID: " + userId + " exists.");
  }
}