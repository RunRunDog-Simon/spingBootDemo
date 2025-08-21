package com.gtalent.demo.responses;

import com.gtalent.demo.model.User;

public class GetUserResponse {
    private int id;
    private String username;
    private  String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public GetUserResponse(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public GetUserResponse() {
    }

    public GetUserResponse(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
