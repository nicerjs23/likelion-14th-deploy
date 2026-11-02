package com.likelion.springsession.comment.controller;

import com.likelion.springsession.comment.dto.CommentCreateRequest;
import com.likelion.springsession.comment.dto.CommentResponse;
import com.likelion.springsession.comment.dto.CommentUpdateRequest;
import com.likelion.springsession.comment.service.CommentService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse create(@AuthenticationPrincipal Long userId,
                                  @PathVariable("postId") Long postId,
                                  @Valid @RequestBody CommentCreateRequest request) {
        return commentService.addComment(userId, postId, request);
    }

    @GetMapping
    public List<CommentResponse> list(@PathVariable("postId") Long postId) {
        return commentService.getComments(postId);
    }

    // 4주차 과제 — 댓글 수정 / 5주차 과제 — 작성자 검사
    @PutMapping("/{commentId}")
    public CommentResponse update(@AuthenticationPrincipal Long userId,
                                  @PathVariable("postId") Long postId,
                                  @PathVariable("commentId") Long commentId,
                                  @Valid @RequestBody CommentUpdateRequest request) {
        return commentService.updateComment(userId, postId, commentId, request);
    }

    // 4주차 과제 — 댓글 삭제 / 5주차 과제 — 작성자 검사
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal Long userId,
                       @PathVariable("postId") Long postId,
                       @PathVariable("commentId") Long commentId) {
        commentService.deleteComment(userId, postId, commentId);
    }
}
