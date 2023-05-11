package com.example.cmproject.repository;

import com.example.cmproject.entity.Post;
import com.example.cmproject.entity.PostLike;
import com.example.cmproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    boolean existsByUserAndPost(User user, Post post);

    PostLike findByUserAndPost(User user, Post post);

}
