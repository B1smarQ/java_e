package org.forum.service;

import org.forum.entity.User;
import org.forum.entity.Session;
import org.forum.repository.SessionRepository;
import org.forum.repository.UserRepository;
import org.forum.util.PasswordUtil;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

  private UserRepository userRepository;
  private SessionRepository sessionRepository;

  public List<User> getUsers() {
    return userRepository.findAll();
  }

  public User saveUser(User user) {
    user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
    return userRepository.save(user);
  }

  public String login(String username, String password) {
    System.out.println("FINDING USER WITH USERNAME " + username);
    User user = userRepository.getUserByUsername(username);
    if (user == null) {
      System.out.println("COULDNT FIND USER " + username);
      return "";
    }

    if (!PasswordUtil.verifyPassword(password, user.getPassword())) {
      System.out.println("PASSWORDS DO NOT MATCH");
      return "";
    }

    String token = UUID.randomUUID().toString();
    sessionRepository.save(new Session(null, token, null, null));
    return token;
  }
}
