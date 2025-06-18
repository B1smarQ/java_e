package org.forum.repository;

import org.forum.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByCommunityId(Integer communityId);
    List<Post> findByAuthorId(Integer authorId);
} 