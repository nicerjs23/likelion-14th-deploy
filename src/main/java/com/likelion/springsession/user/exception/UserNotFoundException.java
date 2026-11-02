package com.likelion.springsession.user.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("회원을 찾을 수 없습니다.");
    }
}
