package com.humanbooster.businesscase.security;

public record AuthRequestDTO(String username, String password) {

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
