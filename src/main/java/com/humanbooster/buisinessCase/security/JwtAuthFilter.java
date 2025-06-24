package com.humanbooster.buisinessCase.security;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.humanbooster.buisinessCase.model.JwtRefresh;
import com.humanbooster.buisinessCase.repository.JwtRefreshRepository;
import com.humanbooster.buisinessCase.repository.UserRepository;
import com.humanbooster.buisinessCase.service.JwtRefreshService;
import com.humanbooster.buisinessCase.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final JwtRefreshService jwtRefreshService;
    private final JwtRefreshRepository jwtRefreshRepository;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

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

        // Get Access Token from Authorization Header
        // Remove Bearer prefix
        final String token = authHeader.substring(7);
        final String username = jwtService.extractUsername(token);
        
        // User not authenticated yet, loads it from DB
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            // Valid Get the Access Token
            // Check Access Token validity, if valid, set the authentication in the context
            if (jwtService.isTokenValid(token, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    // Hides password
                    null,
                    userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // Save auth to the Spring Context
                    SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            // Refresh Token Valid ?
            else{
                Long userId = userRepository.findByUsername(username).get().getId();
                JwtRefresh refreshToken = jwtRefreshRepository.findByUserIdOrderByIssuedAtDesc(userId).get(0);
                System.out.println("REFRESH TOKEN: " + refreshToken.getRefreshToken());
                // Valid generate a new Access Token
                if (jwtRefreshService.isTokenValid(refreshToken.getRefreshToken(), userDetails)) {
                    String newAccessToken = jwtService.generateToken(userDetails.getUsername());
                    // Set the new access token in the response header
                    response.setHeader("Authorization", "Bearer " + newAccessToken);
                }
            }
        }
        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }

}
