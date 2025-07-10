package org.example.judge.auth.configuration;

import org.example.judge.auth.JwtAuthenticationConverter;
import org.example.judge.auth.filter.JwtAuthenticationFilter;
import org.example.judge.auth.provider.JwtAuthenticationProvider;
import org.example.judge.auth.repository.UserRepository;
import org.example.judge.auth.token.TokenBlacklistManager;
import org.example.judge.auth.token.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationConverter;

@Configuration
public class AuthConfiguration {

    @Bean
    public TokenBlacklistManager tokenBlacklistManager() {
        return new TokenBlacklistManager();
    }

    @Bean
    public TokenProvider  tokenProvider() {
        return new TokenProvider();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(TokenBlacklistManager tokenBlacklistManager, TokenProvider tokenProvider) {
        return new JwtAuthenticationConverter(tokenBlacklistManager, tokenProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(userRepository);
        return new ProviderManager(jwtAuthenticationProvider,daoAuthenticationProvider);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationConverter jwtAuthenticationConverter, AuthenticationManager authenticationManager) {
        return new JwtAuthenticationFilter(jwtAuthenticationConverter,authenticationManager);
    }
}