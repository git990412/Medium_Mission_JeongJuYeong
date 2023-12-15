package com.ll.medium.domain.post.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ll.medium.domain.member.member.dto.MemberDto;
import com.ll.medium.domain.post.comment.dto.CommentDto;
import com.ll.medium.domain.post.post.entity.Post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDto {
  private Long id;
  private String title;
  private String body;
  private LocalDateTime createDate;
  private LocalDateTime modifyDate;
  private boolean isPublished;
  private Long hit;
  private MemberDto member;
  private List<CommentDto> comments;

  public PostDto(Post post) {
    this.id = post.getId();
    this.title = post.getTitle();
    this.body = post.getBody();
    this.hit = post.getHit();
    this.isPublished = post.isPublished();
    this.createDate = post.getCreateDate();
    this.modifyDate = post.getModifyDate();
    this.member = new MemberDto(post.getMember());
    comments = post.getComments().stream().map(CommentDto::new).toList();
  }
}
