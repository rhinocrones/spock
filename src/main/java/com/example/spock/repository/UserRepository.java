package com.example.spock.repository;

import com.example.spock.domain.User;
import java.util.List;

public interface UserRepository {

  User findById(String registrationId);

  User getById(String registrationId);

  User save(String emailAddress, String name, String lastName);

  User update(User user);

  User findByEmailAddress(String emailAddress);

  List<User> findAll();
}