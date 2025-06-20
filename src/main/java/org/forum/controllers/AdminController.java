package org.forum.controllers;


import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.forum.entity.AdminData;
import org.forum.entity.Community;
import org.forum.entity.User;
import org.forum.service.*;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@AllArgsConstructor
public class AdminController {

    private SessionService sessionService;
    private UserService userService;
    private LogService logService;

    private CommunityService communityService;
    private PostService postService;

    @RequestMapping(value = "/admin/data")
    public AdminData getAdminData(@RequestHeader(name = "Authorization") String token, HttpServletResponse response){
        String[] tokenParts = token.split(" ");
        if(tokenParts.length != 2){
            response.setStatus(401);
            return null;
        }
        if(!isAdmin(token)){
            response.setStatus(401);
            return null;
        }

        List<User> users = this.userService.getAllUsers();
        List<Community> communities = this.communityService.getAllCommunities();

        return new AdminData(users,communities);


    }

    private boolean isAdmin(String token){

        if(!this.sessionService.isValidToken(token)){
            return false;
        }

        User currUser = this.userService.getUserInfo(token);

        if(currUser != null && "admin".equals(currUser.getUserRole())){
            return true;
        }

        return false;
    }
}
