package org.forum.service;

import lombok.AllArgsConstructor;

import org.forum.entity.Community;
import org.forum.entity.Post;
import org.forum.repository.PostRepository;
import org.forum.repository.UserRepository;
import org.forum.entity.User;
import org.forum.repository.CommunityRepository;
import org.springframework.stereotype.Service;
import org.forum.dto.PostDTO;
import org.forum.repository.CommentRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class PostService {
  private PostRepository repo;
  private UserRepository userRepository;
  private CommunityRepository communityRepository;
  private CommentRepository commentRepository;

  public List<Post> getPosts() {
    return this.repo.findAll();
  }

  public List<Post> getPostsByCommunityId(Integer id) {
    return this.repo.findByCommunityId(id);
  }

  public List<Post> getPostsByAuthorId(Integer id) {
    return this.repo.findByAuthorId(id);
  }

  public boolean createPost(Post post){
      try{
        this.repo.save(post);
        return true;
      }
      catch(Exception e){
        return false;
      }
  }

  public boolean updatePost(Integer postId, String title, String body, Integer userId) {
    Post post = repo.findById(postId).orElse(null);
    if (post == null) return false;
    User user = userRepository.findById(userId).orElse(null);
    if (user == null) return false;
    String role = user.getUserRole();
    if (!post.getAuthorId().equals(userId) && (role == null || !role.equals("admin"))) return false;
    if (title != null) post.setTitle(title);
    if (body != null) post.setBody(body);
    repo.save(post);
    return true;
  }

  public boolean deletePost(Integer postId, Integer userId) {
    Post post = repo.findById(postId).orElse(null);
    if (post == null) return false;
    User user = userRepository.findById(userId).orElse(null);
    if (user == null) return false;
    String role = user.getUserRole();
    if (role == null) return false;
    if (role.equals("admin") || role.equals("mod") || post.getAuthorId().equals(userId)) {
      repo.deleteById(postId);
      return true;
    }
    return false;
  }

  public Post getPostById(Integer postId) {
    return this.repo.findById(postId).orElse(null);
  }

  public PostDTO toDTO(Post post) {
    if (post == null) return null;
    String communityName = null;
    if (post.getCommunityId() != null && communityRepository != null) {
        var community = communityRepository.findById(post.getCommunityId()).orElse(null);
        if (community != null) {
            communityName = community.getName();
        }
    }
    int commentCount = 0;
    if (commentRepository != null && post.getId() != null) {
        commentCount = commentRepository.findByReplyToId(post.getId()).size();
    }
    PostDTO dto = new PostDTO(
        post.getId(),
        post.getTitle(),
        post.getBody(),
        post.getAuthorId(),
        post.getCommunityId(),
        post.getCreationTime(),
        communityName,
        commentCount
    );
    return dto;
  }

  public List<PostDTO> getPostDTOsByCommunityId(Integer communityId) {
    return getPostsByCommunityId(communityId).stream().map(this::toDTO).toList();
  }
}
