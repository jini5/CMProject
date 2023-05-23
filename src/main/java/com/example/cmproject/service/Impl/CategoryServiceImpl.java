package com.example.cmproject.service.Impl;

import com.example.cmproject.dto.CategoryDTO;
import com.example.cmproject.entity.Category;
import com.example.cmproject.repository.CategoryRepository;
import com.example.cmproject.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public ResponseEntity<?> createCategory(CategoryDTO.CreateCategory createDTO) {
        try {
            if (categoryRepository.existsByName(createDTO.getCategoryName())) {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            if (createDTO.getCategoryDepth() != 1) {
                Category parent = categoryRepository.findById(createDTO.getCategoryParent()).orElseThrow(IllegalArgumentException::new);
                if (parent.getCategoryDepth() != createDTO.getCategoryDepth() - 1) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                categoryRepository.save(createDTO.toChild(parent));
            } else {
                categoryRepository.save(createDTO.toParent());
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> selectCategory() {
        try {
            List<CategoryDTO.ViewCategory> list = categoryRepository.findAllByParentIsNull().stream().map(CategoryDTO.ViewCategory::of).collect(Collectors.toList());
            if (list.size() < 1) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateCategory(Long categoryId, CategoryDTO.UpdateCategory updateDTO) {
        try {
            if (categoryRepository.existsByName(updateDTO.getCategoryName())) {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            Category category = categoryRepository.findById(categoryId).orElseThrow(IllegalArgumentException::new);
            category.update(updateDTO.getCategoryName(), updateDTO.getRole());

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> deleteCategory(Long categoryId) {
        try {
            Category category = categoryRepository.findById(categoryId).orElseThrow(IllegalArgumentException::new);
            if (category.getChildren().size() > 0) {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            categoryRepository.delete(category);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> viewCategoryDetail(Long categoryId) {
        try {
            Category category = categoryRepository.findById(categoryId).orElseThrow(IllegalArgumentException::new);
            return new ResponseEntity<>(CategoryDTO.ViewCategory.of(category), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
