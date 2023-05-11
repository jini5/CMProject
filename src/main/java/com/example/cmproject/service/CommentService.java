package com.example.cmproject.service;

import com.example.cmproject.dto.CommentDTO;
import com.example.cmproject.dto.UserDTO;
import org.springframework.http.ResponseEntity;

public interface CommentService {
    ResponseEntity<?> createComment(UserDTO.UserAccessDTO userAccessDTO, CommentDTO.CreateCommentReqDTO createCommentReqDTO, Long postId);

    ResponseEntity<?> createReComment(UserDTO.UserAccessDTO userAccessDTO, CommentDTO.ReCommentReqDTO reCommentReqDTO, Long commentId, Long postId);

    ResponseEntity<?> deleteComment(UserDTO.UserAccessDTO userAccessDTO, Long commentId);

    ResponseEntity<?> updateComment(UserDTO.UserAccessDTO userAccessDTO, CommentDTO.UpdateCommentReqDTO updateCommentReqDTO, Long commentId);

    ResponseEntity<?> likeComment(UserDTO.UserAccessDTO userAccessDTO, Long commentId);

    ResponseEntity<?> disLikeComment(UserDTO.UserAccessDTO userAccessDTO, Long commentId);

    ResponseEntity<?> cancelCommentLike(UserDTO.UserAccessDTO userAccessDTO, Long CommentId);
}
