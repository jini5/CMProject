package com.example.cmproject.repository;

import com.example.cmproject.entity.Comment;
import com.example.cmproject.entity.CommentLike;
import com.example.cmproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    CommentLike findByUserAndComment(User user, Comment comment);

}
