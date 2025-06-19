package org.forum.config;

import lombok.AllArgsConstructor;
import org.forum.entity.*;
import org.forum.repository.*;
import org.forum.service.LogService;
import org.forum.util.PasswordUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {

  private UserRepository userRepository;

  private CommunityRepository communityRepository;

  private PostRepository postRepository;

  private CommentRepository commentRepository;

  private LogService logService;

  @Override
  public void run(String... args) throws Exception {

    if (userRepository.count() == 0) {
      try {

        User user1 = new User();
        user1.setUsername("john_doe");
        user1.setEmail("john@example.com");
        String password = "testUserPassword";
        String hashed = PasswordUtil.hashPassword(password);
        user1.setPassword(hashed);
        userRepository.save(user1);
        logService.saveLog(new Log(null, LogLevels.INFO.getLogLevel(), "Created user" + user1.getUsername(), null));

        User user2 = new User();
        user2.setUsername("jane_smith");
        user2.setEmail("jane@example.com");
        user2.setPassword(PasswordUtil.hashPassword("testUserPassword"));
        userRepository.save(user2);

        logService.saveLog(new Log(null, LogLevels.INFO.getLogLevel(), "Created user" + user2.getUsername(), null));

        User user3 = new User();
        user3.setUsername("admin");
        user3.setEmail("admin@example.com");
        user3.setPassword(PasswordUtil.hashPassword("adminPassword"));
        userRepository.save(user3);

        logService.saveLog(new Log(null, LogLevels.INFO.getLogLevel(), "Created user" + user3.getUsername(), null));

        Community community1 = new Community();
        community1.setName("Technology");
        community1.setCreator(user1);
        community1.setDescription("Discuss the latest in technology");
        communityRepository.save(community1);

        logService.saveLog(new Log(null, LogLevels.INFO.getLogLevel(), "Created a community", null));

        Community community2 = new Community();
        community2.setName("Gaming");
        community2.setCreator(user2);
        community2.setDescription("All about gaming");
        communityRepository.save(community2);

        logService.saveLog(new Log(null, LogLevels.INFO.getLogLevel(), "Created a community", null));

        Post post1 = new Post();
        post1.setTitle("Best Programming Languages in 2024");
        post1.setBody("What are your thoughts on the most in-demand programming languages this year?");
        post1.setAuthorId(user1.getId());
        post1.setCommunityId(community1.getId());
        postRepository.save(post1);

        logService.saveLog(new Log(null, LogLevels.INFO.getLogLevel(), "Created a post", null));

        Post post2 = new Post();
        post2.setTitle("Favorite Video Games");
        post2.setBody("Share your favorite games and why you love them!");
        post2.setAuthorId(user2.getId());
        post2.setCommunityId(community2.getId());
        postRepository.save(post2);

        logService.saveLog(new Log(null, LogLevels.INFO.getLogLevel(), "Created a post", null));

        Comment comment1 = new Comment();
        comment1.setAuthor(user3);
        comment1.setReplyTo(post1);
        comment1.setBody("Python and JavaScript are definitely at the top!");
        commentRepository.save(comment1);

        logService.saveLog(new Log(null, LogLevels.INFO.getLogLevel(), "Created a comment", null));

        Comment comment2 = new Comment();
        comment2.setAuthor(user1);
        comment2.setReplyTo(post2);
        comment2.setBody("I love RPG games, especially open-world ones!");
        commentRepository.save(comment2);

        logService.saveLog(new Log(null, LogLevels.INFO.getLogLevel(), "Created a comment", null));

        System.out.println("Sample data created successfully!");

        logService.saveLog(new Log(null, LogLevels.INFO.getLogLevel(), "Init successfull", null));

      } catch (Exception e) {
        System.out.println("Error creating sample data: " + e);

        logService.saveLog(new Log(null, LogLevels.ERROR.getLogLevel(), "Error creating sample data", null));
      }
    }
  }
}
