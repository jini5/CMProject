package com.example.cmproject.dto;

import com.example.cmproject.entity.Category;
import com.example.cmproject.entity.Post;
import com.example.cmproject.entity.Role;
import com.example.cmproject.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PostDTO {

    @Getter
    @Setter
    @ApiModel(value = "게시글 생성")
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class CreatePostReqDTO {

        @ApiModelProperty(value = "카테고리 ID", required = true)
        private Long categoryId;

        @ApiModelProperty(value = "게시글 제목", required = true)
        private String title;

        @ApiModelProperty(value = "게시글 내용", required = true)
        private String content;

        public Post toEntity(User user, Category category) {
            return Post.builder()
                    .user(user)
                    .category(category)
                    .title(title)
                    .content(content).build();
        }

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @ApiModel(value = "게시글 수정")
    public static class UpdateReqDTO {

        @ApiModelProperty(value = "카테고리 ID", required = true)
        private Long categoryId;

        @ApiModelProperty(value = "글 제목", required = true)
        private String title;

        @ApiModelProperty(value = "글 내용", required = true)
        private String content;

        @ApiModelProperty(value = "게시글 수정일자")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate updatedDate;

    }

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "게시글 삭제")
    public class DeletePostReqDTO {
        @NotNull
        @ApiModelProperty(value = "게시글 아이디")
        private Long postId;
    }

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "게시글 상세 조회")
    public static class PostDetailResDTO {

        @ApiModelProperty(notes = "게시글 ID")
        private Long postId;

        @ApiModelProperty(notes = "작성자 ID")
        private Long userId;

        @ApiModelProperty(notes = "카테고리 ID")
        private Long categoryId;

        @ApiModelProperty(notes = "작성자 닉네임")
        private String userNickname;

        @ApiModelProperty(notes = "카테고리 이름")
        private String categoryName;

        @ApiModelProperty(notes = "게시글 제목")
        private String title;

        @ApiModelProperty(notes = "게시글 내용")
        private String content;

        @ApiModelProperty(notes = "로그인 한 유저의 좋아요 여부")
        private boolean likeStatus;

        @ApiModelProperty(notes = "댓글 리스트")
        private List<CommentDTO.CommentResDTO> commentList;

        @ApiModelProperty(value = "게시글 생성일자")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate createdDate;

        @ApiModelProperty(value = "게시글 수정일자")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate updatedDate;

        @ApiModelProperty(notes = "카테고리 Role")
        private Role categoryRole;

        @Builder
        public PostDetailResDTO(Post post, boolean isLiked) {
            this.postId = post.getPostId();
            this.userId = post.getUser().getUserId();
            this.categoryId = post.getCategory().getCategoryId();
            this.userNickname = post.getUser().getNickName();
            this.categoryName = post.getCategory().getName();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.likeStatus = isLiked;
            this.commentList = getCommentList();
            this.createdDate = getCreatedDate();
            this.updatedDate = getUpdatedDate();
            this.categoryRole = post.getCategory().getRole();
        }

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostResDTO {

        @ApiModelProperty(notes = "게시물 ID")
        private Long postId;

        @ApiModelProperty(notes = "제목")
        private String title;

        @ApiModelProperty(notes = "작성자 닉네임")
        private String nickname;

        @ApiModelProperty(notes = "좋아요 수")
        private int likeCount;

        @ApiModelProperty(notes = "작성일자")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime createdDate;

        @ApiModelProperty(value = "게시글 마지막 수정일자")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime updatedDate;

        public PostResDTO(Post post, User user) {
            this.postId = post.getPostId();
            this.title = post.getTitle();
            this.nickname = user.getNickName();
            this.likeCount = post.getPostLikes().size();
            this.createdDate = post.getCreatedDate();
            this.updatedDate = post.getUpdatedDate();
        }

        public PostResDTO(Post post) {
            this.postId = post.getPostId();
            this.title = post.getTitle();
            this.nickname = post.getUser().getNickName();
            this.likeCount = post.getPostLikes().size();
            this.createdDate = post.getCreatedDate();
            this.updatedDate = post.getUpdatedDate();
        }
    }
}
