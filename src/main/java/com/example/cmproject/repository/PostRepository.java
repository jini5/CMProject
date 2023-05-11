package com.example.cmproject.repository;

import com.example.cmproject.entity.Category;
import com.example.cmproject.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findById(Long postId);

    Page<Post> findByCategoryCategoryId(Long categoryId, String keyword, PageRequest pageRequest);
}
