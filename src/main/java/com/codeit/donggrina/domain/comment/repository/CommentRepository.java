package com.codeit.donggrina.domain.comment.repository;

import com.codeit.donggrina.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
