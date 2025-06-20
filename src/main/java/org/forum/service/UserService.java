package org.forum.service;

import org.forum.entity.Log;
import org.forum.entity.LogLevels;
import org.forum.entity.User;
import org.forum.entity.Session;
import org.forum.repository.LogRepository;
import org.forum.repository.SessionRepository;
import org.forum.repository.UserRepository;
import org.forum.util.PasswordUtil;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class UserService {

  private UserRepository userRepository;
  private SessionRepository sessionRepository;
  private LogRepository logRepository;

  public List<User> getUsers() {
    return userRepository.findAll();
  }

  public User getUserByUsername(String username) {
    return userRepository.getUserByUsername(username);
  
  }

  public User getUserInfo(String token){
    Session currentSession = this.sessionRepository.findByToken(token);
    User currentUser = this.userRepository.findById(currentSession.getUserId()).orElse(null);

    return currentUser;

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
    User loggedInUser = this.userRepository.getUserByUsername(username);
    sessionRepository
        .save(new Session(null, token, new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()+ TimeUnit.HOURS.toMillis(1)),  loggedInUser.getId()));
    logRepository.save(
        new Log(null, LogLevels.INFO.getLogLevel(), "Created a session for user with username: " + username, null));
    return token;
  }

  public List<User> getAllUsers(){
    return this.userRepository.findAll();
  }
}
