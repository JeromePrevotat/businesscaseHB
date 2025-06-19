package com.humanbooster.buisinessCase.security;

public record JwtDTO(String token) {
    public String getToken() {
            return token;
        }
}
