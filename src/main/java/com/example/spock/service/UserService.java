package com.example.spock.service;

import com.example.spock.domain.User;
import java.util.List;

public interface UserService {

  User saveUser(String emailAddress, String name, String lastName);

  User updateUser(User user);

  User findUserById(String userId);

  List<User> getAll();
}
