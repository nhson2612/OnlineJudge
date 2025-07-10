package org.example.judge.auth.token;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenBlacklistManager {
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();
    private final Set<String> blacklistedRefreshTokens = ConcurrentHashMap.newKeySet();

    public boolean isRevokedToken(String token) {
        return blacklistedTokens.contains(token);
    }
    public boolean isRevokedRefreshToken(String token) {
        return blacklistedRefreshTokens.contains(token);
    }
    public void revokeToken(String token) {
        blacklistedTokens.add(token);
    }
    public void revokeRefreshToken(String token) {
        blacklistedRefreshTokens.add(token);
    }
    public void removeTokenFromBlacklist(String token) {
        blacklistedTokens.remove(token);
    }
    public void clear() {
        blacklistedTokens.clear();
        blacklistedRefreshTokens.clear();
    }
}