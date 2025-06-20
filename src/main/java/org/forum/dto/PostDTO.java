package org.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    @Getter @Setter
    private Integer id;
    @Getter @Setter
    private String title;
    @Getter @Setter
    private String body;
    @Getter @Setter
    private Integer authorId;
    @Getter @Setter
    private Integer communityId;
    @Getter @Setter
    private Timestamp creationTime;
    @Getter @Setter
    private String communityName;
    @Getter @Setter
    private Integer commentCount;
} 