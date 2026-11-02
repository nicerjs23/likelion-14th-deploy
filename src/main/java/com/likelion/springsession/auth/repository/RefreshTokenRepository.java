package com.likelion.springsession.auth.repository;

import com.likelion.springsession.auth.entity.RefreshToken;
import com.likelion.springsession.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(User user);

    void deleteByUserId(Long userId);
}
