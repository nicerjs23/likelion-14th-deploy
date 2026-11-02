package com.likelion.springsession.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignUpResponse {

    private final Long id;
    private final String email;
    private final String nickname;
}
