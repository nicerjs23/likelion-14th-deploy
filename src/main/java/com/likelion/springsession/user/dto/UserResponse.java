package com.likelion.springsession.user.dto;

import com.likelion.springsession.user.entity.User;
import lombok.Getter;

// 5주차 선택 과제 — 내 정보 응답 (비밀번호는 절대 내보내지 않는다)
@Getter
public class UserResponse {

    private final Long id;
    private final String email;
    private final String nickname;

    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
    }
}
