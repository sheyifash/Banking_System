package com.example.bankingsystem.Config;

import com.example.bankingsystem.Service.MerchantDetailsService;
import com.example.bankingsystem.Service.JWTService;
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
    ApplicationContext context;

    public JwtFilter(JWTService jwtService, MerchantDetailsService merchantDetailsService, ApplicationContext context) {
        this.jwtService = jwtService;
        this.merchantDetailsService = merchantDetailsService;
        this.context = context;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");
    String token = null;
    String email = null;
    if (authHeader != null && authHeader.startsWith("Bearer")){
        token = authHeader.substring(7);
        email = jwtService.extractEmail(token);
    }
    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null){
        UserDetails userDetails = context.getBean(UserDetailsService.class).loadUserByUsername(email);
    if (jwtService.validateToken(token, userDetails)){
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource()
                            .buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
    }
    filterChain.doFilter(request, response);
    }
}
