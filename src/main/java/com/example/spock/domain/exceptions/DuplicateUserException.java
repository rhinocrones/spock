package com.example.spock.domain.exceptions;

public class DuplicateUserException extends RuntimeException {

  public DuplicateUserException(String emailAddress) {
    super("There already exists a user with email address: " + emailAddress);
  }
}