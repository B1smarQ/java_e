package org.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class CommunityDTO {
    @Getter @Setter
    private Integer id;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String description;
    @Getter @Setter
    private Integer creatorId;
    @Getter @Setter
    private String creatorUsername;
} 