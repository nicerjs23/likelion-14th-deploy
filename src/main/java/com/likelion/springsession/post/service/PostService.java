package com.likelion.springsession.post.service;

import com.likelion.springsession.post.dto.PostCreateRequest;
import com.likelion.springsession.post.dto.PostDetailResponse;
import com.likelion.springsession.post.dto.PostSummaryResponse;
import com.likelion.springsession.post.dto.PostUpdateRequest;
import com.likelion.springsession.post.entity.Post;
import com.likelion.springsession.post.exception.PostAccessDeniedException;
import com.likelion.springsession.post.repository.PostRepository;
import com.likelion.springsession.user.entity.User;
import com.likelion.springsession.user.exception.UserNotFoundException;
import com.likelion.springsession.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<PostSummaryResponse> getPostSummaries() {
        return toSummaryResponses(postRepository.findAll());
    }

    // 5주차 선택 과제 — 로그인한 회원이 쓴 글만
    public List<PostSummaryResponse> getMyPosts(Long userId) {
        User me = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        return toSummaryResponses(postRepository.findAllByAuthor(me));
    }

    private List<PostSummaryResponse> toSummaryResponses(List<Post> posts) {
        List<PostSummaryResponse> responses = new ArrayList<>();

        for (Post post : posts) {
            PostSummaryResponse response = new PostSummaryResponse(
                    post.getId(),
                    post.getTitle(),
                    post.getAuthor().getNickname(),
                    post.getCreatedAt()
            );

            responses.add(response);
        }

        return responses;
    }

    // 게시글 조회 기능 추가
    public PostDetailResponse getPost(Long postId) {
        Post post = findPostById(postId);
        return toDetailResponse(post);
    }

    // Service에 게시글 생성 기능 추가
    @Transactional
    public PostDetailResponse createPost(Long userId, PostCreateRequest request) {

        User author = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Post post = new Post(
                author,
                request.getTitle(),
                request.getContent()
        );

        Post savedPost = postRepository.save(post);

        return toDetailResponse(savedPost);
    }

    @Transactional
    public PostDetailResponse updatePost(
            Long userId,
            Long postId,
            PostUpdateRequest request
    ) {
        Post post = findPostById(postId);

        if (!post.isAuthor(userId)) {
            throw new PostAccessDeniedException();
        }

        post.update(
                request.getTitle(),
                request.getContent()
        );

        return toDetailResponse(post);
    }

    @Transactional
    public void deletePost(Long userId, Long postId) {
        Post post = findPostById(postId);

        if (!post.isAuthor(userId)) {
            throw new PostAccessDeniedException();
        }

        postRepository.delete(post);
    }

    // 반복해서 사용할 조회 코드는 메서드로 분리
    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow();
    }

    // Entity를 Response DTO로 변환하기
    private PostDetailResponse toDetailResponse(Post post) {
        return new PostDetailResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getNickname(),
                post.getCreatedAt()
        );
    }
}
