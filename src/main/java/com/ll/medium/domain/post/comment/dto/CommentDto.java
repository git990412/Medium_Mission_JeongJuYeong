package com.ll.medium.domain.post.comment.dto;

import com.ll.medium.domain.post.comment.entity.Comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private String body;
    private String username;

    public CommentDto(Comment comment) {
        this.body = comment.getBody();
        this.username = comment.getMember().getUsername();
    }
}
