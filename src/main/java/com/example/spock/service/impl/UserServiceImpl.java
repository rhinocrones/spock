package com.example.spock.service.impl;

import com.example.spock.domain.User;
import com.example.spock.repository.UserRepository;
import com.example.spock.service.UserService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  public User saveUser(String emailAddress, String name, String lastName) {
    return userRepository.save(emailAddress, name, lastName);
  }

  public User updateUser(User user) {
    return userRepository.update(user);
  }

  public User findUserById(String userId) {
    return userRepository.findByEmailAddress(userId);
  }

  public List<User> getAll() {
    return userRepository.findAll();
  }
}