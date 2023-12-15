package com.ll.medium.domain.member.member.dto;

import java.util.ArrayList;
import java.util.List;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.post.post.entity.Post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
  private String username;
  private List<Post> posts = new ArrayList<>();

  public MemberDto(Member member) {
    this.username = member.getUsername();
  }
}
