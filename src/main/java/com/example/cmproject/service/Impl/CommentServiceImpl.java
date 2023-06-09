package com.example.cmproject.service.Impl;

import com.example.cmproject.dto.CommentDTO;
import com.example.cmproject.dto.UserDTO;
import com.example.cmproject.entity.*;
import com.example.cmproject.repository.CommentLikeRepository;
import com.example.cmproject.repository.CommentRepository;
import com.example.cmproject.repository.PostRepository;
import com.example.cmproject.repository.UserRepository;
import com.example.cmproject.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Override
    public ResponseEntity<?> createComment(UserDTO.UserAccessDTO userAccessDTO, CommentDTO.CreateCommentReqDTO createCommentReqDTO, Long postId) {
        try {
            User user = userRepository.findByEmail(userAccessDTO.getEmail()).orElseThrow(NoSuchElementException::new);
            Post post = postRepository.findById(postId).orElseThrow(NoSuchElementException::new);

            Comment parentComment = null;
            if (createCommentReqDTO.getParentId() != null) {
                parentComment = commentRepository.findById(createCommentReqDTO.getParentId()).orElse(null);
            }

            Comment comment = Comment.builder()
                    .post(post)
                    .user(user)
                    .parent(parentComment)
                    .content(createCommentReqDTO.getContent())
                    .depth(createCommentReqDTO.getDepth())
                    .createdTime(LocalDateTime.now())
                    .updatedTime(LocalDateTime.now())
                    .deleteCheck(false)
                    .build();
            commentRepository.save(comment);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @Override
    public ResponseEntity<?> deleteComment(UserDTO.UserAccessDTO userAccessDTO, Long commentId) {
        try {
            User user = userRepository.findByEmail(userAccessDTO.getEmail()).orElseThrow(NoSuchElementException::new);
            Comment comment = commentRepository.findById(commentId).orElseThrow(NoSuchElementException::new);

            if (!comment.getUser().getUserId().equals(user.getUserId()) && !user.getRole().equals("ROLE_ADMIN")) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            comment.setDeleteCheck(true);
            commentRepository.save(comment);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateComment(UserDTO.UserAccessDTO userAccessDTO, CommentDTO.UpdateCommentReqDTO updateCommentReqDTO, Long commentId) {
        try {
            User user = userRepository.findByEmail(userAccessDTO.getEmail()).orElseThrow(NoSuchElementException::new);
            Comment comment = commentRepository.findById(commentId).orElseThrow(NoSuchElementException::new);

            if (!comment.getUser().getUserId().equals(user.getUserId())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            comment.update(updateCommentReqDTO);
            commentRepository.save(comment);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @Override
    public ResponseEntity<?> likeComment(UserDTO.UserAccessDTO userAccessDTO, Long commentId) {
        try {
            Comment comment = commentRepository.findById(commentId).orElseThrow(NoSuchElementException::new);
            User user = userRepository.findByEmail(userAccessDTO.getEmail()).orElseThrow(NoSuchElementException::new);

            CommentLike commentLike = commentLikeRepository.findByUserAndComment(user, comment);
            if (commentLike == null) {
                commentLike = CommentLike.builder()
                        .user(user)
                        .comment(comment)
                        .isLiked(true)
                        .build();
            } else if (!Optional.ofNullable(commentLike.getIsLiked()).isPresent() || !commentLike.getIsLiked()) {
                commentLike.setIsLiked(true);
            }

            commentLikeRepository.save(commentLike);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> disLikeComment(UserDTO.UserAccessDTO userAccessDTO, Long commentId) {
        try {
            Comment comment = commentRepository.findById(commentId).orElseThrow(NoSuchElementException::new);
            User user = userRepository.findByEmail(userAccessDTO.getEmail()).orElseThrow(NoSuchElementException::new);

            CommentLike commentLike = commentLikeRepository.findByUserAndComment(user, comment);
            if (commentLike == null) {
                commentLike = CommentLike.builder()
                        .user(user)
                        .comment(comment)
                        .isLiked(false)
                        .build();
            } else if (!Optional.ofNullable(commentLike.getIsLiked()).isPresent() || commentLike.getIsLiked()) {
                commentLike.setIsLiked(false);
            }

            commentLikeRepository.save(commentLike);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> cancelCommentLike(UserDTO.UserAccessDTO userAccessDTO, Long commentId) {
        try {
            Comment comment = commentRepository.findById(commentId).orElseThrow(NoSuchElementException::new);
            User user = userRepository.findByEmail(userAccessDTO.getEmail()).orElseThrow(NoSuchElementException::new);

            CommentLike commentLike = commentLikeRepository.findByUserAndComment(user, comment);
            if (commentLike != null) {
                commentLikeRepository.delete(commentLike);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
