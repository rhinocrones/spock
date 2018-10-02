package com.example.spock.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.example.spock.domain.User;
import com.example.spock.service.UserService;
import com.example.spock.service.dto.ExistingUserDTO;
import com.example.spock.service.dto.NewUserDTO;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {

  private final UserService userService;

  @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ExistingUserDTO post(
      @RequestBody @Valid NewUserDTO newUser) {
    User user = userService.saveUser(
        newUser.getEmailAddress(),
        newUser.getName(), newUser.getLastName()
    );

    return asDTO(user);
  }

  @PutMapping(path = "/{userId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ExistingUserDTO put(
      @RequestBody @Valid NewUserDTO newUser,
      @PathVariable("userId") String userId
  ) {
    User user = userService
        .updateUser(asUser(newUser, userId));
    return asDTO(user);
  }


  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<ExistingUserDTO> getAll() {
    return userService.getAll().stream().map(this::asDTO).collect(
        Collectors.toList());
  }

  private ExistingUserDTO asDTO(User user) {
    return new ExistingUserDTO(
        user.getRegistrationId(),
        user.getEmailAddress(),
        user.getName(),
        user.getLastName()
    );
  }

  private User asUser(NewUserDTO dto, String userId) {
    return new User(userId, dto.getEmailAddress(), dto.getName(),
        dto.getLastName());
  }
}