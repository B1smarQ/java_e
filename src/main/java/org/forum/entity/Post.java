package org.forum.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "posts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "title", nullable = false, length = 128)
    private String title;
    
    @Column(name = "body", length = 512)
    private String body;
    
    @Column(name = "author_id", nullable = false)
    private Integer author;
    
    @Column(name = "community_id", nullable = false)
    private Integer community;
    
    @Column(name = "creation_time")
    private Timestamp creationTime;

    @PrePersist
    protected void onCreate() {
        creationTime = new Timestamp(System.currentTimeMillis());
    }
} 