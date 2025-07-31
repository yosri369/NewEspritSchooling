package com.example.user.service;

import com.example.user.entity.RefreshToken;
import com.example.user.entity.User;
import com.example.user.repository.RefreshTokenRepository;
import com.example.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    // Token validity period (e.g., 7 days)
    private final long refreshTokenDurationSec = 7 * 24 * 60 * 60;

    // Create or update a refresh token for a user
    @Transactional
    public RefreshToken createOrUpdateToken(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Try to find existing refresh token
        Optional<RefreshToken> existingTokenOpt = refreshTokenRepository.findByUser(user);

        RefreshToken refreshToken;
        if (existingTokenOpt.isPresent()) {
            refreshToken = existingTokenOpt.get();
            // Update token string and expiry
            refreshToken.setToken(generateTokenString());
            refreshToken.setExpiryDate(Instant.now().plusSeconds(refreshTokenDurationSec));
        } else {
            refreshToken = new RefreshToken();
            refreshToken.setUser(user);
            refreshToken.setToken(generateTokenString());
            refreshToken.setExpiryDate(Instant.now().plusSeconds(refreshTokenDurationSec));
        }

        return refreshTokenRepository.save(refreshToken);
    }

    // Find refresh token by token string
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token).orElse(null);
    }

    // Delete refresh token by token string
    @Transactional
    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    // Delete refresh token by user (e.g., on logout)
    @Transactional
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    // Helper to generate a secure random token string
    private String generateTokenString() {
        return UUID.randomUUID().toString();
    }
}
