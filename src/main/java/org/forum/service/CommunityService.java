package org.forum.service;

import lombok.AllArgsConstructor;
import org.forum.entity.Community;
import org.forum.entity.User;
import org.forum.repository.CommunityRepository;
import org.forum.repository.UserRepository;
import org.forum.dto.CommunityDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;

    public List<Community> getAllCommunities() {
        return communityRepository.findAll();
    }

    public Community getCommunityById(Integer id) {
        return communityRepository.findById(id).orElse(null);
    }

    public Community createCommunity(String name, String description, Integer creatorId) {
        if (communityRepository.findByName(name) != null) {
            return null;
        }
        User creator = userRepository.findById(creatorId).orElse(null);
        if (creator == null) {
            return null;
        }
        Community community = new Community();
        community.setName(name);
        community.setDescription(description);
        community.setCreator(creator);
        return communityRepository.save(community);
    }

    public Community updateCommunity(Integer id, String name, String description, Integer userId) {
        Community community = communityRepository.findById(id).orElse(null);
        if (community == null || community.getCreator() == null) {
            return null;
        }
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }
        String role = user.getUserRole();
        if (!community.getCreator().getId().equals(userId) && (role == null || !role.equals("admin"))) {
            return null;
        }
        if (name != null) community.setName(name);
        if (description != null) community.setDescription(description);
        return communityRepository.save(community);
    }

    public boolean deleteCommunity(Integer id, Integer userId) {
        Community community = communityRepository.findById(id).orElse(null);
        if (community == null || community.getCreator() == null) {
            return false;
        }
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return false;
        }
        String role = user.getUserRole();
        if (role == null) {
            return false;
        }
        if (role.equals("admin") || community.getCreator().getId().equals(userId)) {
            communityRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public CommunityDTO toDTO(Community community) {
        if (community == null) return null;
        return new CommunityDTO(
            community.getId(),
            community.getName(),
            community.getDescription(),
            community.getCreator() != null ? community.getCreator().getId() : null,
            community.getCreator() != null ? community.getCreator().getUsername() : null
        );
    }

    public List<CommunityDTO> getAllCommunityDTOs() {
        return getAllCommunities().stream().map(this::toDTO).toList();
    }
} 