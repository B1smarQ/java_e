package org.forum.service;

import lombok.AllArgsConstructor;
import org.forum.entity.Comment;
import org.forum.entity.Post;
import org.forum.entity.User;
import org.forum.repository.CommentRepository;
import org.forum.repository.PostRepository;
import org.forum.repository.UserRepository;
import org.forum.dto.CommentDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<Comment> getCommentsByPostId(Integer postId) {
        return commentRepository.findByReplyToId(postId);
    }

    public Comment getCommentById(Integer commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    public Comment createComment(Integer postId, Integer authorId, String body, Integer replyToCommentId) {
        User author = userRepository.findById(authorId).orElse(null);
        Post post = postRepository.findById(postId).orElse(null);
        if (author == null || post == null) return null;
        Comment replyToComment = null;
        if (replyToCommentId != null) {
            replyToComment = commentRepository.findById(replyToCommentId).orElse(null);
        }
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setReplyTo(post);
        comment.setReplyToComment(replyToComment);
        comment.setBody(body);
        return commentRepository.save(comment);
    }

    public boolean deleteComment(Integer commentId, Integer userId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) return false;
        if (!comment.getAuthor().getId().equals(userId)) return false;
        commentRepository.deleteById(commentId);
        return true;
    }

    public Comment updateComment(Integer commentId, String body, Integer userId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) return null;
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return null;
        String role = user.getUserRole();
        if (!comment.getAuthor().getId().equals(userId) && (role == null || !role.equals("admin"))) return null;
        comment.setBody(body);
        return commentRepository.save(comment);
    }

    public List<CommentDTO> getCommentDTOsByPostId(Integer postId) {
        List<Comment> comments = getCommentsByPostId(postId);
        return comments.stream().map(this::toDTO).toList();
    }

    public CommentDTO toDTO(Comment comment) {
        if (comment == null) return null;
        CommentDTO dto = new CommentDTO();
        dto.setCommentId(comment.getId());
        dto.setPostId(comment.getReplyTo() != null ? comment.getReplyTo().getId() : null);
        dto.setAuthorId(comment.getAuthor() != null ? comment.getAuthor().getId() : null);
        dto.setBody(comment.getBody());
        dto.setCreationTime(comment.getCreationTime() != null ? comment.getCreationTime().toLocalDateTime() : null);
        dto.setAuthorUsername(comment.getAuthor() != null ? comment.getAuthor().getUsername() : null);
        return dto;
    }

    public List<Comment> getCommentsByAuthorId(Integer authorId) {
        return commentRepository.findByAuthorId(authorId);
    }
} 