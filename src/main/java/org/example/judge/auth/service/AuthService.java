package org.example.judge.auth.service;

import org.example.judge.auth.JwtAuthentication;
import org.example.judge.auth.domain.AuthResponse;
import org.example.judge.auth.domain.Role;
import org.example.judge.auth.domain.User;
import org.example.judge.auth.repository.UserRepository;
import org.example.judge.auth.token.TokenBlacklistManager;
import org.example.judge.auth.token.TokenProvider;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final TokenBlacklistManager tokenBlacklistManager;

    public AuthService(UserRepository userRepository, TokenProvider tokenProvider, TokenBlacklistManager tokenBlacklistManager) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.tokenBlacklistManager = tokenBlacklistManager;
    }

    public AuthResponse register(String username, String password) {
        if(userRepository.existsByUsername(username)){
            throw new IllegalArgumentException("Username already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(Role.STUDENT);
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        user.setClasses(null);

        User saved = userRepository.save(user);
        JwtAuthentication authenticated = JwtAuthentication.authenticated(saved.getId(), saved.getUsername(), saved.getAuthorities());
        String accessToken = tokenProvider.createAccessToken(authenticated);
        String refreshToken = tokenProvider.createRefreshToken(authenticated);
        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        JwtAuthentication authenticated = JwtAuthentication.authenticated(user.getId(), user.getUsername(), user.getAuthorities());
        String accessToken = tokenProvider.createAccessToken(authenticated);
        String refreshToken = tokenProvider.createRefreshToken(authenticated);
        return new AuthResponse(accessToken, refreshToken);
    }

    public void logout(String token) {
        if (token == null || token.isEmpty() || !tokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }
        TokenBlacklistManager blacklistManager = new TokenBlacklistManager();
        blacklistManager.revokeToken(token);
    }

    public AuthResponse refreshToken(String refreshToken) {
        if(refreshToken == null || refreshToken.isEmpty() || !tokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        String userId = tokenProvider.getSubject(refreshToken);
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        JwtAuthentication authenticated = JwtAuthentication.authenticated(user.getId(), user.getUsername(), user.getAuthorities());
        String newAccessToken = tokenProvider.createAccessToken(authenticated);
        String newRefreshToken = tokenProvider.createRefreshToken(authenticated);
        tokenBlacklistManager.revokeToken(refreshToken);
        return new AuthResponse(newAccessToken, newRefreshToken);
    }
}
