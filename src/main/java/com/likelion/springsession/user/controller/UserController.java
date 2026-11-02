package com.likelion.springsession.user.controller;

import com.likelion.springsession.user.dto.UserResponse;
import com.likelion.springsession.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 5주차 선택 과제 — GET /api/users/me (인증 필요: anyRequest().authenticated() 에 걸림)
    @GetMapping("/me")
    public UserResponse me(@AuthenticationPrincipal Long userId) {
        return userService.getMe(userId);
    }
}
