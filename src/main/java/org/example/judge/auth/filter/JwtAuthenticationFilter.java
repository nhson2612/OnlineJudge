package org.example.judge.auth.filter;

import jakarta.servlet.ServletException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.example.judge.auth.JwtAuthenticationConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Log log = LogFactory.getLog(JwtAuthenticationFilter.class);
    private final AuthenticationConverter jwtAuthenticationConverter;
    private final AuthenticationManager authenticationManager;
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();
    private static final List<String> EXCLUDED_PATHS = List.of(
        "/api/auth/login",
        "/api/auth/register",
        "/api/auth/refresh",
        "/api/auth/logout",
        "/api/docs/**",
        "/api/swagger-ui/**",
        "/api/auth/reset-password",
        "/api/auth/forgot-password",
        "/api/auth/update-password"
    );

    public JwtAuthenticationFilter(AuthenticationConverter jwtAuthenticationConverter, AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return EXCLUDED_PATHS.stream().anyMatch(p -> pathMatcher.match(p, request.getServletPath()));
    }

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws ServletException, IOException {
        try {
            Authentication authRequest = jwtAuthenticationConverter.convert(request);
            if(authRequest == null){
                log.warn("No authentication token found in request");
                filterChain.doFilter(request, response);
                return;
            }
            String userId = authRequest.getName();
            if(this.authenticationIsRequired(userId)){
                Authentication authResult = authenticationManager.authenticate(authRequest);
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authResult);
                SecurityContextHolder.setContext(context);
            }
        }catch (AuthenticationException e){
            SecurityContextHolder.clearContext();
            log.warn("Authentication failed: " + e.getMessage(), e);
            return;
        }
        filterChain.doFilter(request, response);
    }
    private boolean authenticationIsRequired(String userId) {
        Authentication existAuthentication = SecurityContextHolder.getContext().getAuthentication();
        return existAuthentication!=null && existAuthentication.getName().equals(userId);
    }
}
