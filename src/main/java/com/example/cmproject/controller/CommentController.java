package com.example.cmproject.controller;


import com.example.cmproject.dto.CommentDTO;
import com.example.cmproject.dto.UserDTO;
import com.example.cmproject.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"댓글 서비스"}, description = "댓글 작성, 수정, 삭제")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CommentController {

    private final CommentService commentService;


    @ApiOperation(value = "댓글 작성", notes = "댓글 작성.\n\n" + "code: 200 조회 성공, 400 잘못된 postId 요청")
    @PostMapping("/{postId}/comments")
    public ResponseEntity<?> createComment(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO,@RequestBody CommentDTO.CreateCommentReqDTO createCommentReqDTO,@PathVariable Long postId) {
        return commentService.createComment(userAccessDTO,createCommentReqDTO,postId);
    }

    @ApiOperation(value = "댓글 삭제", notes = "댓글, 대댓글 삭제.\n\n" + "code: 200 조회 성공, 400 잘못된 commentId 요청")
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO,@PathVariable Long commentId) {
        return commentService.deleteComment(userAccessDTO,commentId);
    }

    @ApiOperation(value = "댓글 수정", notes = "댓글, 대댓글 수정.\n\n" + "code: 200 조회 성공, 400 잘못된 commentId 요청")
    @PatchMapping("/comments/{commentId}/update")
    public ResponseEntity<?> updateComment(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO, @RequestBody CommentDTO.UpdateCommentReqDTO updateCommentReqDTO,@PathVariable Long commentId) {
        return commentService.updateComment(userAccessDTO, updateCommentReqDTO,commentId);
    }

    @ApiOperation(value = "댓글 좋아요", notes = "댓글에 대해 좋아요 버튼을 누른다.\n\n" + "code: 200 조회 성공, 400 잘못된 commentId 요청")
    @GetMapping("/comments/{commentId}/like")
    public ResponseEntity<?> LikeComment(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO, @PathVariable Long commentId) {
        return commentService.likeComment(userAccessDTO, commentId);
    }

    @ApiOperation(value = "댓글  싫어요", notes = "댓글에 대해 싫어요 버튼을 누른다.\n\n" + "code: 200 조회 성공, 400 잘못된 commenttId 요청")
    @GetMapping("/comments/{commentId}/dislike")
    public ResponseEntity<?> disLikeComment(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO, @PathVariable Long commentId) {
        return commentService.disLikeComment(userAccessDTO, commentId);
    }

    @ApiOperation(value = "댓글  좋아요/싫어요 삭제", notes = "댓글에 대해 싫어요 버튼을 누른다.\n\n" + "code: 200 조회 성공, 400 잘못된 commentId 요청")
    @GetMapping("/comments/{commentId}/cancel")
    public ResponseEntity<?> cancelCommentLike(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO, @PathVariable Long commentId) {
        return commentService.cancelCommentLike(userAccessDTO, commentId);
    }

}
