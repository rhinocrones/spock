package com.example.spock.repository.impl;

import com.example.spock.domain.User;
import com.example.spock.domain.exceptions.DuplicateUserException;
import com.example.spock.domain.exceptions.UserNotFoundException;
import com.example.spock.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

@Component
public class InMemoryUserRepository implements UserRepository {

  private final SecureRandom secureRandom = new SecureRandom();
  private final ConcurrentHashMap<String, User> storage = new ConcurrentHashMap<>();


  @Override
  public User findById(String userId) {
    return storage.get(userId);
  }

  @Override
  public User getById(String userId) {
    if (!storage.containsKey(userId)) {
      throw new UserNotFoundException(userId);
    }

    return storage.get(userId);
  }

  @Override
  public User save(String emailAddress, String name, String lastName) {
    if (findByEmailAddress(emailAddress) != null) {
      throw new DuplicateUserException(emailAddress);
    }

    User user = new User(
        generateId(emailAddress),
        emailAddress,
        name,
        lastName
    );

    storage.put(user.getRegistrationId(), user);
    return user;
  }

  @Override
  public User update(User user) {
    if (!storage.containsKey(user.getRegistrationId())) {
      throw new UserNotFoundException(user.getRegistrationId());
    }

    storage.put(user.getRegistrationId(), user);
    return user;
  }

  @Override
  public User findByEmailAddress(String emailAddress) {
    return storage.values().stream().filter(it -> it.getEmailAddress().equals(emailAddress))
        .findFirst().orElse(null);
  }

  @Override
  public List<User> findAll() {
    return new ArrayList<>(storage.values());
  }

  private String generateId(String emailAddress) {
    int salt = secureRandom.nextInt();
    return DigestUtils
        .md5DigestAsHex(String.format("%d%s", salt, emailAddress).getBytes(StandardCharsets.UTF_8))
        .substring(0, 8);
  }
}