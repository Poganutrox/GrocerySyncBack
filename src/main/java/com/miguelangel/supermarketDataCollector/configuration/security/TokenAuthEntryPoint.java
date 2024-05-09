package com.miguelangel.supermarketDataCollector.configuration.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TokenAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/json");
        String message;
        if (authException instanceof BadCredentialsException) {
            message = "Invalid credentials";
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        } else if (authException instanceof LockedException) {
            message = "Your account has been locked.";
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else if (authException instanceof DisabledException) {
            message = "Your account has been disabled.";
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else if (authException instanceof AccountExpiredException) {
            message = "Your account has expired.";
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else if (authException instanceof CredentialsExpiredException) {
            message = "Your credentials have expired.";
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else if (authException instanceof UsernameNotFoundException) {
            message = "User not found.";
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else if (authException instanceof AuthenticationServiceException) {
            message = "Authentication service is not available.";
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            message = "Authentication failed: " + authException.getMessage();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        String json = String.format("{\"message\": \"%s\"}", message);
        response.getWriter().write(json);
    }
}