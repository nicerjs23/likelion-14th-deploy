package com.likelion.springsession.post.entity;

import com.likelion.springsession.comment.entity.Comment;
import com.likelion.springsession.user.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA가 사용하는 기본 생성자
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // 게시글을 새로 만들 때 사용하는 생성자
    public Post(User author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    // 게시글 수정에 사용
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 4주차 과제 — 댓글 목록에서 제거하면 orphanRemoval로 DB에서도 삭제된다
    public void removeComment(Comment comment) {
        this.comments.remove(comment);
    }

    public boolean isAuthor(Long userId) {
        return this.author.getId().equals(userId);
    }
}
