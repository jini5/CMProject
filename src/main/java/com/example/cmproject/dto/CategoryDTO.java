package com.example.cmproject.dto;

import com.example.cmproject.entity.Category;
import com.example.cmproject.entity.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "카테고리 생성", description = "이름,단계,부모 입력")
    public static class CreateCategory {
        @ApiModelProperty(value = "생성할 카테고리 이름", required = true)
        private String categoryName;
        @ApiModelProperty(value = "생성할 카테고리 단계 ( 1: 대분류, 2: 중분류, 3: 소분류 )", required = true)
        private int categoryDepth;
        @ApiModelProperty(value = "생성할 카테고리 부모 ID( 생성할 카테고리가 중분류, 소분류인 경우 필요, 대분류의 경우 아무값 넣어도 상관 없음.)", required = true)
        private Long categoryParent;
        @ApiModelProperty(value = "카테고리 권한", required = true)
        private Role role;

        public Category toChild(Category categoryParent) {
            return new Category(categoryName, categoryParent, categoryDepth, role);
        }

        public Category toParent() {
            return new Category(categoryName, categoryDepth, role);
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "카테고리 조회")
    public static class ViewCategory {
        private Long categoryId;
        private String categoryName;
        private int categoryDepth;
        private Role role;
        private List<ViewCategory> children;

        public static ViewCategory of(Category category) {
            return new ViewCategory(
                    category.getCategoryId(),
                    category.getName(),
                    category.getCategoryDepth(),
                    category.getRole(),
                    category.getChildren().stream().map(ViewCategory::of).collect(Collectors.toList())
            );
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "카테고리 수정")
    public static class UpdateCategory {
        @ApiModelProperty(value = "카테고리 새 이름", required = true)
        private String categoryName;
        @ApiModelProperty(value = "카테고리 권한", required = true)
        private Role role;

    }

}
