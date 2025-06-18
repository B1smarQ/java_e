package org.forum.repository;

import org.forum.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByAuthorId(Integer authorId);
    List<Comment> findByReplyToId(Integer postId);
    List<Comment> findByReplyToCommentId(Integer commentId);
} 