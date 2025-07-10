package org.example.judge.auth.token;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.example.judge.auth.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class TokenProvider {
    private static final Log log = LogFactory.getLog(TokenProvider.class);
    private final String jwtSecret = "";
    private final long accessTokenExpiration = 864_000_000; // 10 days in milliseconds
    private final long refreshTokenExpiration = 864_000_000;


    public String createAccessToken(Authentication authentication) {
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("roles", roles)
                .claim("username", (String) authentication.getCredentials())
                .setIssuedAt(new java.util.Date())
                .setExpiration(new java.util.Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String createRefreshToken(Authentication authentication) {
        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(new java.util.Date())
                .setExpiration(new java.util.Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        }catch (SignatureException e) {
            log.warn("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.warn("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims string is empty.");
        }
        return false;
    }

    public String getSubject(String token) {
        try {
            return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
        } catch (Exception e) {
            log.warn("Failed to get subject from token: " + e.getMessage());
            return null;
        }
    }

    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.warn("Failed to parse claims from token: " + e.getMessage());
            return null;
        }
    }

    public List<GrantedAuthority> getAuthorities(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null && claims.containsKey("roles")) {
            List<String> roles = claims.get("roles", List.class);
            return roles.stream()
                    .map(role -> (GrantedAuthority) () -> role)
                    .toList();
        }
        return List.of();
    }

    public List<String> getRolesFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null && claims.containsKey("roles")) {
            return claims.get("roles", List.class);
        }
        return List.of();
    }
    public Date getExpirationDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    public Date getIssuedAtDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getIssuedAt();
    }

    public boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    public boolean validateToken(String token, User user) {
        String userId = getSubject(token);
        return (userId.equals(user.getUsername()) && !isTokenExpired(token));
    }
    public Map<String, Object> getAllClaimsFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims;
    }
}
