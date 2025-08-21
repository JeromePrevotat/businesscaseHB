package com.humanbooster.buisinessCase.security;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.humanbooster.buisinessCase.exception.AccessTokenExpiredException;
import com.humanbooster.buisinessCase.exception.InvalidTokenException;
import com.humanbooster.buisinessCase.service.RefreshTokenService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        // Retrieve Header and skip to the next filter if incorrect or missing
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        try{
            // Get Access Token from Authorization Header
            // Remove Bearer prefix
            final String token = authHeader.substring(7);
            final String username = refreshTokenService.extractUsername(token);
            
            // User not authenticated yet, loads it from DB
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                // Check Access Token validity, if valid, set the authentication in the context
                if (refreshTokenService.isTokenValid(token, userDetails)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        // Hides password
                        null,
                        userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        // Save auth to the Spring Context
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (ExpiredJwtException e) {
            handleTokenError(response, "TOKEN_EXPIRED", "Access Token has expired", "REFRESH_TOKEN");
            return;
        } catch (JwtException e) {
            handleTokenError(response, "INVALID_TOKEN", "Token is invalid or malformed", "LOGIN_REQUIRED");
            return;
        }
        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }

    private void handleTokenError(HttpServletResponse response, String error, String message, String action) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        
        // CORS Headers
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
        
        Map<String, Object> errorResponse = Map.of(
            "error", error,
            "message", message,
            "action", action
        );
        
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
