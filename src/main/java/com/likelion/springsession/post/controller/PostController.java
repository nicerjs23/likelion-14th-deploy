package com.likelion.springsession.post.controller;

import com.likelion.springsession.post.dto.PostCreateRequest;
import com.likelion.springsession.post.dto.PostDetailResponse;
import com.likelion.springsession.post.dto.PostSummaryResponse;
import com.likelion.springsession.post.dto.PostUpdateRequest;
import com.likelion.springsession.post.service.PostService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts") // 공통 URL 경로를 지정
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<PostSummaryResponse> getPosts() {
        return postService.getPostSummaries();
    }

    // 5주차 선택 과제 — 내 글 목록 (SecurityConfig 에서 인증 필요로 지정)
    @GetMapping("/my")
    public List<PostSummaryResponse> getMyPosts(@AuthenticationPrincipal Long userId) {
        return postService.getMyPosts(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDetailResponse createPost(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody PostCreateRequest request
    ) {
        return postService.createPost(userId, request);
    }

    @GetMapping("/{postId}")
    public PostDetailResponse getPost(
            @PathVariable("postId") Long postId
    ) {
        return postService.getPost(postId);
    }

    @PutMapping("/{postId}")
    public PostDetailResponse updatePost(
            @AuthenticationPrincipal Long userId,
            @PathVariable("postId") Long postId,
            @Valid @RequestBody PostUpdateRequest request
    ) {
        return postService.updatePost(userId, postId, request);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(
            @AuthenticationPrincipal Long userId,
            @PathVariable("postId") Long postId
    ) {
        postService.deletePost(userId, postId);
    }
}
