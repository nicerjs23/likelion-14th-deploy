package com.likelion.springsession.post.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

// 게시글 목록 화면에 필요한 데이터만 담는 Response DTO
@Getter // Getter 추가
@RequiredArgsConstructor // final 필드에 대한 생성자 추가
public class PostSummaryResponse {
    private final Long id;
    private final String title;
    private final String authorNickname;
    private final LocalDateTime createdAt;
}
