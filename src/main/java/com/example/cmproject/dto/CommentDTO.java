package com.example.cmproject.dto;

import com.example.cmproject.entity.Comment;
import com.example.cmproject.entity.Post;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Builder
public class CommentDTO {

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "댓글 작성")
    public static class CreateCommentReqDTO {

        @ApiModelProperty(value = "게시물", required = true)
        private Post post;
        @ApiModelProperty(value = "댓글 내용", required = true)
        private String content;

        @Builder
        public CreateCommentReqDTO(Post post, String content) {
            this.post = post;
            this.content = content;
        }
    }

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "대댓글 작성")
    public static class ReCommentReqDTO {
        @ApiModelProperty(value = "댓글 내용", required = true)
        private String content;
        @ApiModelProperty(value = "부모 댓글 ID", required = true)
        private Long parentId;
    }


    @Getter
    @NoArgsConstructor
    @ApiModel(value = "댓글 수정")
    public static class UpdateCommentReqDTO {
        @ApiModelProperty(value = "댓글 내용", required = true)
        private String content;

    }

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "댓글 조회")
    public static class CommentResDTO {
        @ApiModelProperty(value = "댓글 ID")
        private Long commentId;
        @ApiModelProperty(value = "댓글 내용")
        private String content;
        @ApiModelProperty(value = "작성자 ID")
        private Long userId;
        @ApiModelProperty(value = "작성자 이름")
        private String userName;
        @ApiModelProperty(value = "작성자 이메일")
        private String userEmail;
        @ApiModelProperty(value = "댓글 작성 시간")
        private LocalDateTime createdTime;
        @ApiModelProperty(value = "댓글 수정 시간")
        private LocalDateTime updatedTime;
        @ApiModelProperty(value = "좋아요 수")
        private int likeCount;
        @ApiModelProperty(value = "좋아요 여부")
        private boolean likeStatus;
        @ApiModelProperty(value = "자식 댓글 목록")
        private List<CommentResDTO> children = new ArrayList<>();
    }


}
