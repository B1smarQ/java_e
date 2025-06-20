package org.forum.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import java.sql.Timestamp;

import org.forum.entity.Log;
import org.forum.entity.LogLevels;
import org.forum.entity.User;
import org.forum.service.LogService;
import org.forum.service.SessionService;
import org.forum.service.UserService;
import org.forum.dto.LoginRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class AuthController {
  private UserService userService;
  private LogService logService;

  private SessionService sessionService;

  @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
  public String loginHandle(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
    String username = loginRequest.getUsername();
    String password = loginRequest.getPassword();
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

  @PostMapping("/auth/register")
  public String registerHandle(@RequestBody User user, HttpServletResponse response) {
      System.out.println(user);
    String username = user.getUsername();
    String password = user.getPassword();
    String email = user.getEmail();

    User existingUser = userService.getUserByUsername(username);
    if (existingUser != null) {
        response.setStatus(409);
        return null;
    }

    try {
        User newUser = new User(null, username, password, email, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "user");
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

  @RequestMapping(value = "/auth/me", method = RequestMethod.GET)
  public User getUser(@RequestHeader(name = "Authorization") String token, HttpServletResponse response){

    String[] tokenParts = token.split(" ");
    if(tokenParts.length != 2){
      System.out.println("INVALID TOKEN FORMAT, expected ");
      response.setStatus(400);
      return null;
    }

    if(!this.sessionService.isValidToken(tokenParts[1])){
      System.out.println("INVALID TOKEN");
      response.setStatus(401);
      return null;
    }

    return this.userService.getUserInfo(tokenParts[1]);

  }

}
