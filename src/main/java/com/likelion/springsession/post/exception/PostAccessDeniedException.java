package com.likelion.springsession.post.exception;

public class PostAccessDeniedException extends RuntimeException {

    public PostAccessDeniedException() {
        super("본인이 작성한 게시글만 수정하거나 삭제할 수 있습니다.");
    }
}
