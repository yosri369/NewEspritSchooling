package com.example.user.controller;

import com.example.user.dto.LoginRequest;
import com.example.user.dto.LoginResponse;
import com.example.user.entity.RefreshToken;
import com.example.user.security.JwtUtils;
import com.example.user.security.UserDetailsImpl;
import com.example.user.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;

    private final boolean isProduction = false; // change to true in prod

    // 1. LOGIN endpoint
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest req,
            HttpServletResponse response
    ) {
        // Authenticate user credentials
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password())
        );

        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        String username = userDetails.getUsername();
        String role = userDetails.getUser().getRole().getRoleType().name();

        // Issue tokens and set cookies
        return issueTokensAndSetCookies(userDetails.getUser().getId(), username, role, response);
    }

    // 2. REFRESH endpoint
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshTokenStr,
            HttpServletResponse response
    ) {
        if (refreshTokenStr == null) {
            System.out.println("🚫 Aucun cookie refreshToken reçu !");
            return ResponseEntity.status(401).build();
        }
        System.out.println("🔁 Reçu refreshToken: " + refreshTokenStr);

        RefreshToken storedToken = refreshTokenService.findByToken(refreshTokenStr);
        if (storedToken == null) {
            System.out.println("🚫 Token introuvable en base !");
            return ResponseEntity.status(401).build();
        }

        if (storedToken.getExpiryDate().isBefore(Instant.now())) {
            System.out.println("🚫 Token expiré !");
            return ResponseEntity.status(401).build();
        }

        String username = storedToken.getUser().getUsername();
        String role = storedToken.getUser().getRole().getRoleType().name();
        Long userId = storedToken.getUser().getId();

        // Issue new access token and refresh token cookie (optional: refresh token rotation)
        return issueTokensAndSetCookies(userId, username, role, response);
    }

    // 3. LOGOUT endpoint
    @Transactional
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshTokenStr,
            HttpServletResponse response
    ) {
        if (refreshTokenStr != null) {
            refreshTokenService.deleteByToken(refreshTokenStr);
        }

        // Clear both refreshToken (HttpOnly) and accessToken cookies
        ResponseCookie clearRefresh = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(isProduction)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        ResponseCookie clearAccess = ResponseCookie.from("accessToken", "")
                .httpOnly(false)
                .secure(isProduction)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, clearRefresh.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, clearAccess.toString());

        return ResponseEntity.ok().build();
    }

    // Helper method to issue access and refresh tokens and set cookies accordingly
    private ResponseEntity<LoginResponse> issueTokensAndSetCookies(
            Long userId,
            String username,
            String role,
            HttpServletResponse response
    ) {
        String accessToken = jwtUtils.generateAccessToken(username, role);
        String refreshToken = refreshTokenService.createOrUpdateToken(userId).getToken();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(isProduction)
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7 days
                .sameSite("Lax")
                .build();

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(false)
                .secure(isProduction)
                .path("/")
                .maxAge(60) // 1 minute (adjust as needed)
                .sameSite("Lax")
                .build();

        // Set cookies in response header
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());

        // Return access token and role in response body for client use
        return ResponseEntity.ok(new LoginResponse(accessToken, role));
    }
}
