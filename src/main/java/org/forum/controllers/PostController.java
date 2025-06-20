package org.forum.controllers;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.forum.service.LogService;
import org.forum.service.PostService;
import org.forum.service.SessionService;
import org.forum.service.UserService;
import org.forum.service.CommentService;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import org.forum.entity.Log;
import org.forum.entity.LogLevels;
import org.forum.entity.Post;
import org.forum.entity.Comment;
import org.forum.dto.CommentDTO;
import org.forum.dto.PostDTO;
import org.forum.entity.User;

@RestController
@AllArgsConstructor
public class PostController {
  private UserService uService;
  private LogService lService;
  private PostService pService;
  private SessionService sService;
  private CommentService commentService;

  @GetMapping("/posts")
  public List<PostDTO> getPosts(HttpServletResponse response) {
    try {
        List<PostDTO> posts = this.pService.getPosts().stream().map(this.pService::toDTO).toList();
        response.setStatus(200);
        return posts;
    } catch (Exception e) {
        response.setStatus(500);
        return null;
    }
  }

  @RequestMapping(value = "/posts/community/{id}", method = RequestMethod.GET)
  public List<Post> getCommunityPosts(@PathVariable("id") Integer id, HttpServletResponse response) {
    try{List<Post> posts = this.pService.getPostsByCommunityId(id);
        response.setStatus(200);
        return posts;
    }
    catch (Exception e){
        response.setStatus(500);
        this.lService.saveLog(new Log(null,LogLevels.ERROR.getLogLevel(),"Failed to get posts from community with id: "+id,new Timestamp(System.currentTimeMillis())));
        return null;
    }
  }


  @PostMapping("/posts")
  public void addPost(@RequestBody PostDTO postDTO, @RequestHeader("Authorization") String token, HttpServletResponse response) {
    try {
        String[] tokenParts = token.split(" ");
        if (tokenParts.length != 2) {
            response.setStatus(400);
            response.setHeader("Error", "Incorrect token format");
            return;
        }
        String actualToken = tokenParts[1];
        if (!this.sService.isValidToken(actualToken)) {
            response.setStatus(401);
            return;
        }
        Integer userId = this.sService.getUserIdFromToken(actualToken);
        if (userId == null) {
            response.setStatus(401);
            return;
        }
        Post newPost = new Post(null, postDTO.getTitle(), postDTO.getBody(), userId, postDTO.getCommunityId(), null);
        boolean res = this.pService.createPost(newPost);
        if (!res) {
            response.setStatus(500);
            return;
        }
        response.setStatus(201);
    } catch (Exception e) {
        response.setStatus(500);
        this.lService.saveLog(new Log(null, LogLevels.ERROR.getLogLevel(), "Failed to create post: " + e.getMessage(), new Timestamp(System.currentTimeMillis())));
    }
  }

  @PutMapping("/posts/{id}")
  public void updatePost(@PathVariable("id") Integer id,
                        @RequestParam(value = "title", required = false) String title,
                        @RequestParam(value = "body", required = false) String body,
                        @RequestHeader("Authorization") String token,
                        HttpServletResponse response) {
    String[] tokenParts = token.split(" ");
    if (tokenParts.length != 2) {
      response.setStatus(400);
      return;
    }
    Integer userId = this.sService.getUserIdFromToken(tokenParts[1]);
    if (userId == null) {
      response.setStatus(401);
      return;
    }
    boolean updated = this.pService.updatePost(id, title, body, userId);
    if (!updated) {
      response.setStatus(403);
      return;
    }
    response.setStatus(200);
  }

  @DeleteMapping("/posts/{id}")
  public void deletePost(@PathVariable("id") Integer id,
                        @RequestHeader("Authorization") String token,
                        HttpServletResponse response) {
    String[] tokenParts = token.split(" ");
    if (tokenParts.length != 2) {
      response.setStatus(400);
      return;
    }
    Integer userId = this.sService.getUserIdFromToken(tokenParts[1]);
    if (userId == null) {
      response.setStatus(401);
      return;
    }
    boolean deleted = this.pService.deletePost(id, userId);
    if (!deleted) {
      response.setStatus(403);
      return;
    }
    response.setStatus(204);
  }

  @GetMapping("/posts/{id}")
  public Post getPostById(@PathVariable("id") Integer id){
    return this.pService.getPostById(id);
  }

  @GetMapping("/posts/{id}/comments")
  public List<CommentDTO> getComments(@PathVariable("id") Integer postId, HttpServletResponse response) {
    try {
      List<CommentDTO> comments = commentService.getCommentDTOsByPostId(postId);
      response.setStatus(200);
      return comments;
    } catch (Exception e) {
      response.setStatus(500);
      return null;
    }
  }

  @PostMapping("/posts/{id}/comments")
  public CommentDTO addApiComment(@PathVariable("id") Integer postId,
                                  @RequestBody CommentDTO commentDTO,
                                  HttpServletResponse response) {
    Comment comment = commentService.createComment(
        postId,
        commentDTO.getAuthorId(),
        commentDTO.getBody(),
        null
    );
    if (comment == null) {
        response.setStatus(400);
        return null;
    }
    response.setStatus(201);
    return commentService.toDTO(comment);
  }

  @PutMapping("/posts/{postId}/comments/{commentId}")
  public CommentDTO updateComment(@PathVariable("postId") Integer postId,
                              @PathVariable("commentId") Integer commentId,
                              @RequestBody CommentDTO commentDTO,
                              @RequestHeader("Authorization") String token,
                              HttpServletResponse response) {
    String[] tokenParts = token.split(" ");
    if (tokenParts.length != 2) {
      response.setStatus(400);
      return null;
    }
    String actualToken = tokenParts[1];
    if (!this.sService.isValidToken(actualToken)) {
      response.setStatus(401);
      return null;
    }
    Integer userId = this.sService.getUserIdFromToken(actualToken);
    if (userId == null) {
      response.setStatus(401);
      return null;
    }
    Comment updated = commentService.updateComment(commentId, commentDTO.getBody(), userId);
    if (updated == null) {
      response.setStatus(403);
      return null;
    }
    response.setStatus(200);
    return commentService.toDTO(updated);
  }

  @DeleteMapping("/posts/{postId}/comments/{commentId}")
  public void deleteComment(@PathVariable("postId") Integer postId,
                           @PathVariable("commentId") Integer commentId,
                           @RequestHeader("Authorization") String token,
                           HttpServletResponse response) {
    String[] tokenParts = token.split(" ");
    if (tokenParts.length != 2) {
      response.setStatus(400);
      return;
    }
    String actualToken = tokenParts[1];
    if (!this.sService.isValidToken(actualToken)) {
      response.setStatus(401);
      return;
    }
    Integer userId = this.sService.getUserIdFromToken(actualToken);
    if (userId == null) {
      response.setStatus(401);
      return;
    }
    boolean deleted = commentService.deleteComment(commentId, userId);
    if (!deleted) {
      response.setStatus(403);
      return;
    }
    response.setStatus(204);
  }

  @GetMapping("/posts/user/{userId}")
  public List<PostDTO> getPostsByUser(@PathVariable("userId") Integer userId, HttpServletResponse response) {
    try {
        List<PostDTO> posts = this.pService.getPostsByAuthorId(userId).stream().map(this.pService::toDTO).toList();
        response.setStatus(200);
        return posts;
    } catch (Exception e) {
        response.setStatus(500);
        return null;
    }
  }

  @GetMapping("/users/{userId}/info")
  public Map<String, Object> getUserInfo(@PathVariable("userId") Integer userId, HttpServletResponse response) {
    try {
        User user = this.uService.getUsers().stream().filter(u -> u.getId().equals(userId)).findFirst().orElse(null);
        if (user == null) {
            response.setStatus(404);
            return null;
        }
        Map<String, Object> result = new HashMap<>();
        result.put("username", user.getUsername());
        result.put("role", user.getUserRole());
        result.put("creationTime", user.getCreatedAt());
        response.setStatus(200);
        return result;
    } catch (Exception e) {
        response.setStatus(500);
        return null;
    }
  }

  @GetMapping("/comments/user/{userId}")
  public List<CommentDTO> getCommentsByUser(@PathVariable("userId") Integer userId, HttpServletResponse response) {
    try {
        List<CommentDTO> comments = commentService.getCommentsByAuthorId(userId)
            .stream().map(commentService::toDTO).toList();
        response.setStatus(200);
        return comments;
    } catch (Exception e) {
        response.setStatus(500);
        return null;
    }
  }
}
