package org.forum.entity;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class AdminData {

    public List<User> users;
    public List<Community> communities;

}
