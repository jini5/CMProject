package com.example.cmproject.service.Impl;

import com.example.cmproject.dto.PostDTO;
import com.example.cmproject.dto.UserDTO;
import com.example.cmproject.entity.Category;
import com.example.cmproject.entity.Post;
import com.example.cmproject.entity.PostLike;
import com.example.cmproject.entity.User;
import com.example.cmproject.repository.*;
import com.example.cmproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.example.cmproject.global.config.PageSizeConfig.POST_LIST_SIZE;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PostLikeRepository postLikeRepository;



    @Override
    public ResponseEntity<?> createPost(UserDTO.UserAccessDTO userAccessDTO, PostDTO.CreatePostReqDTO createPostReqDTO) {
        try {
            User user = userRepository.findByEmail(userAccessDTO.getEmail()).orElseThrow(NoSuchElementException::new);
            Category category = categoryRepository.findByCategoryId(createPostReqDTO.getCategoryId()).orElseThrow(NoSuchElementException::new);
            Post post = createPostReqDTO.toEntity(user, category);

            if(!user.getRole().equals("ROLE_ADMIN")){
                if(category.getRole().equals("ROLE_ADMIN_WRITE")){
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
            }

            postRepository.save(post);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (NoSuchElementException e) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> updatePost(UserDTO.UserAccessDTO userAccessDTO, PostDTO.UpdateReqDTO updateReqDTO, Long postId) {
        try {
            Post post = postRepository.findById(postId).orElseThrow(NoSuchElementException::new);
            Category category = categoryRepository.findByCategoryId(post.getCategory().getCategoryId()).orElseThrow(NoSuchElementException::new);
            User user = userRepository.findByEmail(userAccessDTO.getEmail()).orElseThrow(NoSuchElementException::new);

            if(!post.getUser().getUserId().equals(user.getUserId())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            post.update(updateReqDTO.getTitle(), updateReqDTO.getContent(), category);
            postRepository.save(post);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> deletePost(UserDTO.UserAccessDTO userAccessDTO, Long postId) {
        try {
            Post post = postRepository.findById(postId).orElseThrow(NoSuchElementException::new);
            User user = userRepository.findByEmail(userAccessDTO.getEmail()).orElseThrow(NoSuchElementException::new);

            if (!post.getUser().getUserId().equals(user.getUserId()) && !user.getRole().equals("ROLE_ADMIN")) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            postRepository.delete(post);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public ResponseEntity<?> findPostList(Long categoryId, String keyword, int pageNumber) {
        try {
            PageRequest pageRequest = PageRequest.of(pageNumber - 1, POST_LIST_SIZE);
            Page<Post> postPage = postRepository.findByCategoryCategoryId(categoryId, keyword, pageRequest);
            if (postPage == null) {
                throw new NullPointerException();
            }
            if (postPage.getTotalElements() < 1) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Page<PostDTO.PostResDTO> postListResDTO = postPage.map(post -> new PostDTO.PostResDTO(post, post.getUser()));
            return new ResponseEntity<>(postListResDTO, HttpStatus.OK);

        } catch (NoSuchElementException e) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> findDetail(UserDTO.UserAccessDTO userAccessDTO,Long postId) {
        try {
            Post post = postRepository.findById(postId).orElseThrow(NoSuchElementException::new);
            User user = userRepository.findByEmail(userAccessDTO.getEmail()).orElseThrow(NoSuchElementException::new);

            boolean isLiked = postLikeRepository.existsByUserAndPost(user, post);

            PostDTO.PostDetailResDTO postDetailResDTO = PostDTO.PostDetailResDTO.builder()
                    .post(post)
                    .isLiked(isLiked)
                    .build();

            return new ResponseEntity<>(postDetailResDTO,HttpStatus.OK);

        } catch (NoSuchElementException e) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> likePost(UserDTO.UserAccessDTO userAccessDTO, Long postId) {
        try {
            Post post = postRepository.findById(postId).orElseThrow(NoSuchElementException::new);
            User user = userRepository.findByEmail(userAccessDTO.getEmail()).orElseThrow(NoSuchElementException::new);

            PostLike postLike = postLikeRepository.findByUserAndPost(user, post);
            if (postLike == null) {
                postLike = PostLike.builder()
                        .user(user)
                        .post(post)
                        .isLiked(true)
                        .build();
            } else if (!Optional.ofNullable(postLike.getIsLiked()).isPresent() || !postLike.getIsLiked()) {
                postLike.setIsLiked(true);
            }

            postLikeRepository.save(postLike);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> dislikePost(UserDTO.UserAccessDTO userAccessDTO, Long postId) {
        try {
            Post post = postRepository.findById(postId).orElseThrow(NoSuchElementException::new);
            User user = userRepository.findByEmail(userAccessDTO.getEmail()).orElseThrow(NoSuchElementException::new);

            PostLike postLike = postLikeRepository.findByUserAndPost(user, post);
            if (postLike == null) {
                postLike = PostLike.builder()
                        .user(user)
                        .post(post)
                        .isLiked(false)
                        .build();
            } else if (!Optional.ofNullable(postLike.getIsLiked()).isPresent() || postLike.getIsLiked()) {
                postLike.setIsLiked(false);
            }

            postLikeRepository.save(postLike);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> cancelLikePost(UserDTO.UserAccessDTO userAccessDTO, Long postId) {
        try {
            Post post = postRepository.findById(postId).orElseThrow(NoSuchElementException::new);
            User user = userRepository.findByEmail(userAccessDTO.getEmail()).orElseThrow(NoSuchElementException::new);

            PostLike postLike = postLikeRepository.findByUserAndPost(user, post);
            if (postLike != null) {
                postLikeRepository.delete(postLike);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }




}
