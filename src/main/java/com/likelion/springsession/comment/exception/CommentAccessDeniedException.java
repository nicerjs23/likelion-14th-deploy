package com.likelion.springsession.comment.exception;

// 5주차 과제 — 다른 사람의 댓글을 수정·삭제하려 할 때
public class CommentAccessDeniedException extends RuntimeException {

    public CommentAccessDeniedException() {
        super("본인이 작성한 댓글만 수정하거나 삭제할 수 있습니다.");
    }
}
