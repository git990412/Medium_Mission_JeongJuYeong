package com.ll.medium.domain.post.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.medium.domain.post.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
