package com.example.cmproject.service;

import com.example.cmproject.dto.CategoryDTO;
import org.springframework.http.ResponseEntity;

public interface CategoryService {

    ResponseEntity<?> createCategory(CategoryDTO.CreateCategory createDTO);

    ResponseEntity<?> selectCategory();

    ResponseEntity<?> updateCategory(Long categoryId, CategoryDTO.UpdateCategory updateDTO);

    ResponseEntity<?> deleteCategory(Long categoryId);

    ResponseEntity<?> viewCategoryDetail(Long categoryId);
}
