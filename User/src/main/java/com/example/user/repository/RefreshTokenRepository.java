package com.example.user.repository;

import com.example.user.entity.RefreshToken;
import com.example.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);

    @Transactional
    @Modifying
    @Query("delete from RefreshToken rt where rt.token = ?1")
    void deleteByToken(String token);

    @Transactional
    @Modifying
    @Query("delete from RefreshToken rt where rt.user = ?1")
    void deleteByUser(User user);
}
