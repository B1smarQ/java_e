package org.forum.controllers;

import org.forum.entity.Community;
import org.forum.service.CommunityService;
import org.forum.service.SessionService;
import org.forum.dto.CommunityDTO;
import org.forum.dto.PostDTO;
import org.forum.service.PostService;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@AllArgsConstructor
public class CommunityController {
    private final CommunityService communityService;
    private final SessionService sessionService;
    private final PostService postService;

    @GetMapping("/communities")
    public List<CommunityDTO> getAllCommunities(HttpServletResponse response) {
        try {
            List<CommunityDTO> communities = communityService.getAllCommunityDTOs();
            response.setStatus(200);
            return communities;
        } catch (Exception e) {
            response.setStatus(500);
            return null;
        }
    }

    @GetMapping("/communities/{id}")
    public CommunityDTO getCommunityById(@PathVariable("id") Integer id, HttpServletResponse response) {
        try {
            CommunityDTO community = communityService.toDTO(communityService.getCommunityById(id));
            if (community == null) {
                response.setStatus(404);
                return null;
            }
            response.setStatus(200);
            return community;
        } catch (Exception e) {
            response.setStatus(500);
            return null;
        }
    }

    @PostMapping("/communities")
    public CommunityDTO createCommunity(@RequestBody CommunityDTO communityDTO, HttpServletResponse response) {
        try {
            Community community = communityService.createCommunity(
                communityDTO.getName(),
                communityDTO.getDescription(),
                communityDTO.getCreatorId()
            );
            if (community == null) {
                response.setStatus(409); // Conflict
                return null;
            }
            CommunityDTO dto = communityService.toDTO(community);
            response.setStatus(201);
            return dto;
        } catch (Exception e) {
            response.setStatus(500);
            return null;
        }
    }

    @PutMapping("/communities/{id}")
    public CommunityDTO updateCommunity(@PathVariable("id") Integer id,
                                     @RequestParam(value = "name", required = false) String name,
                                     @RequestParam(value = "description", required = false) String description,
                                     @RequestHeader("Authorization") String token,
                                     HttpServletResponse response) {
        String[] tokenParts = token.split(" ");
        if (tokenParts.length != 2) {
            response.setStatus(400);
            return null;
        }
        Integer userId = sessionService.getUserIdFromToken(tokenParts[1]);
        if (userId == null) {
            response.setStatus(401);
            return null;
        }
        CommunityDTO updated = communityService.toDTO(communityService.updateCommunity(id, name, description, userId));
        if (updated == null) {
            response.setStatus(403);
            return null;
        }
        response.setStatus(200);
        return updated;
    }

    @DeleteMapping("/communities/{id}")
    public void deleteCommunity(@PathVariable("id") Integer id,
                                @RequestHeader("Authorization") String token,
                                HttpServletResponse response) {
        String[] tokenParts = token.split(" ");
        if (tokenParts.length != 2) {
            response.setStatus(400);
            return;
        }
        Integer userId = sessionService.getUserIdFromToken(tokenParts[1]);
        if (userId == null) {
            response.setStatus(401);
            return;
        }
        boolean deleted = communityService.deleteCommunity(id, userId);
        if (!deleted) {
            response.setStatus(403);
            return;
        }
        response.setStatus(204);
    }

    @GetMapping("/communities/{id}/posts")
    public List<PostDTO> getCommunityPosts(@PathVariable("id") Integer id, HttpServletResponse response) {
        try {
            List<PostDTO> posts = postService.getPostDTOsByCommunityId(id);
            response.setStatus(200);
            return posts;
        } catch (Exception e) {
            response.setStatus(500);
            return null;
        }
    }
}