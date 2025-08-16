package com.gtalent.demo.responses;

public class UpdateUserRequest {
    private String Username;

    public UpdateUserRequest(String username) {
        Username = username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getUsername() {
        return Username;
    }
}
