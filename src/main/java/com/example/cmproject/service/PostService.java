package com.example.cmproject.service;

import com.example.cmproject.dto.PostDTO;
import com.example.cmproject.dto.UserDTO;
import com.example.cmproject.entity.Category;
import com.example.cmproject.entity.SearchType;
import org.springframework.http.ResponseEntity;

public interface PostService {

    ResponseEntity<?> findPostList(Long categoryId, int pageNumber);

    ResponseEntity<?> findDetail(UserDTO.UserAccessDTO userAccessDTO, Long postId);

    ResponseEntity<?> createPost(UserDTO.UserAccessDTO userAccessDTO, PostDTO.CreatePostReqDTO createPostReqDTO);

    ResponseEntity<?> updatePost(UserDTO.UserAccessDTO userAccessDTO, PostDTO.UpdateReqDTO updateReqDTO, Long postId);

    ResponseEntity<?> deletePost(UserDTO.UserAccessDTO userAccessDTO, Long postId);


    ResponseEntity<?> likePost(UserDTO.UserAccessDTO userAccessDTO, Long postId);

    ResponseEntity<?> dislikePost(UserDTO.UserAccessDTO userAccessDTO, Long postId);

    ResponseEntity<?> cancelLikePost(UserDTO.UserAccessDTO userAccessDTO, Long postId);

    ResponseEntity<?> findPostByKeywordAndType(String keyword, int page, SearchType type);
}
