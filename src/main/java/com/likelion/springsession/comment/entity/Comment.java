package com.likelion.springsession.comment.entity;

import com.likelion.springsession.post.entity.Post;
import com.likelion.springsession.user.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    public Comment(Post post, User author, String content) {
        this.post = post;
        this.author = author;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        post.getComments().add(this);   // 반대쪽 리스트에도 나를 추가
    }

    // 4주차 과제 — 댓글 내용만 수정하고 작성 시각은 유지
    public void update(String content) {
        this.content = content;
    }

    // 4주차 과제 — 이 게시글의 댓글인지 확인
    public boolean belongsTo(Long postId) {
        return this.post.getId().equals(postId);
    }

    public boolean isAuthor(Long userId) {
        return this.author.getId().equals(userId);
    }
}
