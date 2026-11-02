package com.likelion.springsession.comment.service;

import com.likelion.springsession.comment.dto.CommentCreateRequest;
import com.likelion.springsession.comment.dto.CommentResponse;
import com.likelion.springsession.comment.dto.CommentUpdateRequest;
import com.likelion.springsession.comment.entity.Comment;
import com.likelion.springsession.comment.exception.CommentAccessDeniedException;
import com.likelion.springsession.comment.repository.CommentRepository;
import com.likelion.springsession.post.entity.Post;
import com.likelion.springsession.post.repository.PostRepository;
import com.likelion.springsession.user.entity.User;
import com.likelion.springsession.user.exception.UserNotFoundException;
import com.likelion.springsession.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponse addComment(Long userId, Long postId, CommentCreateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow();

        User author = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Comment saved = commentRepository.save(new Comment(post, author, request.getContent()));
        return new CommentResponse(saved);
    }

    public List<CommentResponse> getComments(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow();

        List<CommentResponse> responses = new ArrayList<>();
        for (Comment comment : commentRepository.findAllByPost(post)) {
            responses.add(new CommentResponse(comment));
        }
        return responses;
    }

    // 4주차 과제 — 댓글 수정 (save() 없이 변경 감지)
    // 5주차 과제 — 작성자만 수정 가능
    @Transactional
    public CommentResponse updateComment(Long userId, Long postId, Long commentId, CommentUpdateRequest request) {
        Comment comment = findCommentOfPost(postId, commentId);

        if (!comment.isAuthor(userId)) {
            throw new CommentAccessDeniedException();
        }

        comment.update(request.getContent());

        return new CommentResponse(comment);
    }

    // 4주차 과제 — 댓글 삭제 (컬렉션에서 제거 → orphanRemoval)
    // 5주차 과제 — 작성자만 삭제 가능
    @Transactional
    public void deleteComment(Long userId, Long postId, Long commentId) {
        Comment comment = findCommentOfPost(postId, commentId);

        if (!comment.isAuthor(userId)) {
            throw new CommentAccessDeniedException();
        }

        comment.getPost().removeComment(comment);
    }

    // 존재하는 게시글의 댓글인지 확인한다. 다른 게시글의 댓글 ID면 진행하지 않는다.
    private Comment findCommentOfPost(Long postId, Long commentId) {
        postRepository.findById(postId)
                .orElseThrow();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow();

        if (!comment.belongsTo(postId)) {
            throw new NoSuchElementException("해당 게시글의 댓글이 아닙니다.");
        }
        return comment;
    }
}
