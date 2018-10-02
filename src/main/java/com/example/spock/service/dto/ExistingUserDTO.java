package com.example.spock.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExistingUserDTO {

  private final String userId;

  private final String emailAddress;

  private final String name;

  private final String lastName;

  @JsonProperty("user_id")
  public String getUserId() {
    return userId;
  }

  @JsonProperty("email_address")
  public String getEmailAddress() {
    return emailAddress;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("last_name")
  public String getLastName() {
    return lastName;
  }
}