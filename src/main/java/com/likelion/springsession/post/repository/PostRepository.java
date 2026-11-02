package com.likelion.springsession.post.repository;

import com.likelion.springsession.post.entity.Post;
import com.likelion.springsession.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository 상속
public interface PostRepository extends JpaRepository<Post, Long> {

    // 5주차 선택 과제 — 특정 회원이 쓴 글만
    List<Post> findAllByAuthor(User author);
}
