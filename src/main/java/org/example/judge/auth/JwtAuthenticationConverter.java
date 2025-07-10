package org.example.judge.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.example.judge.auth.token.TokenBlacklistManager;
import org.example.judge.auth.token.TokenProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.List;


public class JwtAuthenticationConverter implements AuthenticationConverter {

    private static final Log LOG = LogFactory.getLog(JwtAuthenticationConverter.class);
    private final TokenBlacklistManager tokenBlacklistManager;
    private final TokenProvider tokenProvider;

    public JwtAuthenticationConverter(TokenBlacklistManager tokenBlacklistManager, TokenProvider tokenProvider) {
        this.tokenBlacklistManager = tokenBlacklistManager;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if(header == null){
            return null;
        }else {
            if(!StringUtils.startsWithIgnoreCase(header, "Bearer ")){
                LOG.warn("Invalid Authorization header format: " + header);
                return null;
            }else if(header.equalsIgnoreCase("Bearer ")){
                throw new BadCredentialsException("Invalid Authorization header format");
            }else {
                String token = header.substring(7);
                if(tokenBlacklistManager.isRevokedToken(token)) {
                    LOG.warn("Token is revoked: " + token);
                    throw new BadCredentialsException("Token is revoked");
                }

                String subject = tokenProvider.getSubject(token);
                Long userId;
                try {
                    userId = Long.parseLong(subject);
                } catch (NumberFormatException e) {
                    LOG.warn("Invalid token subject: " + subject, e);
                    throw new BadCredentialsException("Invalid token subject");
                }
                List<GrantedAuthority> authorities = tokenProvider.getAuthorities(token);
                if(userId == null){
                    throw new BadCredentialsException("Invalid token subject");
                }else {
                    JwtAuthentication jwtAuthentication = new JwtAuthentication(userId,token ,authorities);
                    return jwtAuthentication;
                }
            }
        }
    }
}
