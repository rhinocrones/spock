package com.example.spock.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class User {

  private final String registrationId;

  private final String emailAddress;

  private final String name;

  private final String lastName;
}