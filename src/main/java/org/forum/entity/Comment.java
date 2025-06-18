package org.forum.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_to")
    private Post replyTo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_to_comment")
    private Comment replyToComment;
    
    @Column(name = "body", nullable = false, length = 256)
    private String body;
    
    @Column(name = "creation_time")
    private Timestamp creationTime;

    @PrePersist
    protected void onCreate() {
        creationTime = new Timestamp(System.currentTimeMillis());
    }
} 