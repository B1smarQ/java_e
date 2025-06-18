package org.forum.service;

import lombok.AllArgsConstructor;
import org.forum.entity.Post;
import org.forum.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PostService {
    private PostRepository repo;
    public List<Post> getPosts(){
        return this.repo.findAll();
    }
}
