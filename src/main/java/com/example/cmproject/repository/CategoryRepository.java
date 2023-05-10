package com.example.cmproject.repository;

import com.example.cmproject.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByParentIsNull();

    boolean existsByName(String name);


    Optional<Category> findByCategoryId(Long categoryId);
}
