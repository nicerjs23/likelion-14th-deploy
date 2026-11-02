package com.likelion.springsession.auth.service;

import com.likelion.springsession.auth.dto.LoginRequest;
import com.likelion.springsession.auth.dto.ReissueRequest;
import com.likelion.springsession.auth.dto.SignUpRequest;
import com.likelion.springsession.auth.dto.SignUpResponse;
import com.likelion.springsession.auth.dto.TokenResponse;
import com.likelion.springsession.auth.entity.RefreshToken;
import com.likelion.springsession.auth.exception.DuplicateEmailException;
import com.likelion.springsession.auth.exception.InvalidCredentialsException;
import com.likelion.springsession.auth.exception.InvalidRefreshTokenException;
import com.likelion.springsession.auth.repository.RefreshTokenRepository;
import com.likelion.springsession.security.JwtProvider;
import com.likelion.springsession.user.entity.User;
import com.likelion.springsession.user.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = userRepository.save(
                new User(request.getEmail(), encodedPassword, request.getNickname()));

        return new SignUpResponse(user.getId(), user.getEmail(), user.getNickname());
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return issueTokens(user);
    }

    @Transactional
    public TokenResponse reissue(ReissueRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new InvalidRefreshTokenException();
        }

        RefreshToken saved = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(InvalidRefreshTokenException::new);

        return issueTokens(saved.getUser());
    }

    @Transactional
    public void logout(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    private TokenResponse issueTokens(User user) {
        String accessToken = jwtProvider.createAccessToken(user.getId());
        String refreshToken = jwtProvider.createRefreshToken(user.getId());
        LocalDateTime expiresAt = jwtProvider.getExpiresAt(refreshToken);

        RefreshToken saved = refreshTokenRepository.findByUser(user).orElse(null);

        if (saved == null) {
            refreshTokenRepository.save(new RefreshToken(user, refreshToken, expiresAt));
        } else {
            saved.update(refreshToken, expiresAt);
        }

        return new TokenResponse(accessToken, refreshToken);
    }
}
