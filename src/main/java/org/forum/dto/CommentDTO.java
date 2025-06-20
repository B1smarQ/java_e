package org.forum.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CommentDTO {
    private Integer commentId;
    private Integer postId;
    private Integer authorId;
    private String body;
    private LocalDateTime creationTime;
    private String authorUsername;

}