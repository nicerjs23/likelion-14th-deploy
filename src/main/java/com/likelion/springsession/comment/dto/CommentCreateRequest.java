package com.likelion.springsession.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class CommentCreateRequest {

    @NotBlank(message = "댓글 내용은 비어 있을 수 없습니다.")
    @Size(max = 500, message = "댓글은 500자를 넘을 수 없습니다.")
    private String content;
}
