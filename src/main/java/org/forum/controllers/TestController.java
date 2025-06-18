package org.forum.controllers;

import lombok.AllArgsConstructor;
import org.forum.entity.Post;
import org.forum.entity.User;
import org.forum.service.PostService;
import org.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class TestController {
  private UserService service;
  private PostService postService;


  @GetMapping
  public String hello() {
    return "Hello world!";
  }

  @RequestMapping("/users")
  public List<User> getUsers() {
    return service.getUsers();
  }

  @RequestMapping("/posts")
  public List<Post> getPosts(){
    return this.postService.getPosts();
  }
}
