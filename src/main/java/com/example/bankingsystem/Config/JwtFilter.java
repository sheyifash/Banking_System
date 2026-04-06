package com.example.bankingsystem.Config;

import com.example.bankingsystem.Exception.JwtExpired;
import com.example.bankingsystem.Service.MerchantDetailsService;
import com.example.bankingsystem.Service.JWTService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
public class JwtFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final MerchantDetailsService merchantDetailsService;
    private final ApplicationContext context;

    public JwtFilter(JWTService jwtService,
                     MerchantDetailsService merchantDetailsService,
                     ApplicationContext context) {
        this.jwtService = jwtService;
        this.merchantDetailsService = merchantDetailsService;
        this.context = context;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        // 1. Skip JWT check for public endpoints
        if (path.startsWith("/api/auth/login")
                || path.startsWith("/api/auth/register")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-resources")
                || path.startsWith("/webjars")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Normal JWT processing
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;
try {
    if (authHeader != null && authHeader.startsWith("Bearer ")) { // note the space
        token = authHeader.substring(7);
        email = jwtService.extractEmail(token);
    }

    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails =
                context.getBean(UserDetailsService.class).loadUserByUsername(email);

        if (jwtService.validateToken(token, userDetails)) {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
    }
} catch (Exception e) {
    System.out.println("JWT ERROR: " + e.getMessage());
    throw new JwtExpired("Session expired, please logIn!");
}
        filterChain.doFilter(request, response);
    }
}