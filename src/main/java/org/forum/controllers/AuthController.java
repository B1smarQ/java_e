package org.forum.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.UUID;

import org.forum.entity.Log;
import org.forum.entity.LogLevels;
import org.forum.entity.User;
import org.forum.service.LogService;
import org.forum.service.UserService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {
  private UserService userService;
  private LogService logService;

  @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
  public String loginHandle(@RequestParam(name = "username") String username,
      @RequestParam(name = "password") String password, HttpServletResponse response) {

    String token = userService.login(username, password);
    if ("".equals(token)) {
      response.setStatus(400);
      long currentTime = System.currentTimeMillis();
      logService
          .saveLog(new Log(null, LogLevels.INFO.getLogLevel(), "Failed login request from user: " + username,
              new Timestamp(currentTime)));
      return null;
    }
    response.setStatus(200);
    return token;
  }

  @RequestMapping(value = "/auth/register", method = RequestMethod.POST)
  public String registerHandle(@RequestParam(name = "username") String username,
      @RequestParam(name = "password") String password,
      @RequestParam(name = "email") String email, HttpServletResponse response) {

    try {
      User newUser = new User(null, username, password, email, null, null);
      User savedUser = userService.saveUser(newUser);
      
      if (savedUser != null) {
        response.setStatus(201);
        long currentTime = System.currentTimeMillis();
        logService.saveLog(new Log(null, LogLevels.INFO.getLogLevel(), 
            "New user registered: " + username, new Timestamp(currentTime)));
        return "User registered successfully";
      } else {
        response.setStatus(400);
        return "Registration failed";
      }
    } catch (Exception e) {
      response.setStatus(400);
      long currentTime = System.currentTimeMillis();
      logService.saveLog(new Log(null, LogLevels.ERROR.getLogLevel(), 
          "Registration error for user: " + username + " - " + e.getMessage(), 
          new Timestamp(currentTime)));
      return "Registration failed: " + e.getMessage();
    }
  }
}
