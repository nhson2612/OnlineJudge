package org.example.judge.auth.provider;

import org.example.judge.auth.JwtAuthentication;
import org.example.judge.auth.domain.User;
import org.example.judge.auth.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;

    public JwtAuthenticationProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Long userId;
        try {
            userId = Long.parseLong(authentication.getName());
        } catch (NumberFormatException e) {
            throw new BadCredentialsException("Invalid user ID in token");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        return JwtAuthentication.authenticated(
                user.getId(),
                user.getUsername(),
                user.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthentication.class.isAssignableFrom(authentication);
    }
}
