package com.likelion.springsession.comment.repository;

import com.likelion.springsession.comment.entity.Comment;
import com.likelion.springsession.post.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);
}
