package com.example.cmproject.repository;

import com.example.cmproject.entity.Post;
import com.example.cmproject.entity.SearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostCustomRepository {
    Page<Post> searchByKeywordAndType(Pageable pageable, String keyword, SearchType type);
}
