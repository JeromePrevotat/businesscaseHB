package com.humanbooster.businesscase.security;

public record JwtDTO(String token) {
    public String getToken() {
            return token;
        }
}
