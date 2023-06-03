package com.example.cmproject.controller;


import com.example.cmproject.dto.PostDTO;
import com.example.cmproject.dto.UserDTO;
import com.example.cmproject.entity.SearchType;
import com.example.cmproject.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"게시판 서비스"}, description = "게시글 목록 조회, 게시글 상세 정보 조회, 게시글 추가, 게시글 권한 확인, 게시글 수정, 게시글 삭제")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @ApiOperation(value = "게시글 추가", notes = "게시글을 추가한다.\n\n" + "code: 201 추가 성공, 400 잘못된 user 토큰 정보 요청, 403 권한없는 사용자 접근")
    @PostMapping
    public ResponseEntity<?> createPost(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO, @RequestBody PostDTO.CreatePostReqDTO createPostReqDTO) {
        return postService.createPost(userAccessDTO, createPostReqDTO);
    }

    @ApiOperation(value = "게시글 수정", notes = "게시글을 수정한다.\n\n" + "code: 200 수정 성공, 400 잘못된 boardId 요청, 403 권한없는 사용자 접근")
    @PatchMapping("/{postId}/update")
    public ResponseEntity<?> updatePost(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO, @RequestBody PostDTO.UpdateReqDTO updateReqDTO, @PathVariable Long postId) {
        return postService.updatePost(userAccessDTO, updateReqDTO, postId);
    }

    @ApiOperation(value = "게시글 삭제", notes = "게시글을 삭제한다.\n\n" + "code: 201 추가 성공, 400 잘못된 boardId 요청, 403 권한없는 사용자 접근")
    @PostMapping("/{postId}")
    public ResponseEntity<?> deletePost(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO, @PathVariable Long postId) {
        return postService.deletePost(userAccessDTO, postId);
    }

    @ApiOperation(value = "게시글 목록 조회", notes = "현재 페이지의 게시판별 게시글 목록을 조회한다.\n\n" + "code: 200 조회 성공, 204 조회 성공 + 표시할 내용 없음, 500 알 수 없는 서버 오류")
    @GetMapping("/{categoryId}")
    public ResponseEntity<?> findList(@PathVariable Long categoryId,
                                      @RequestParam(required = false, defaultValue = "1") int pageNumber) {
        return postService.findPostList(categoryId, pageNumber);
    }

    @ApiOperation(value = "게시글 상세 정보 조회", notes = "게시글 상세 정보를 조회한다.\n\n" + "code: 200 조회 성공, 400 잘못된 postId 요청")
    @GetMapping("/detail/{postId}")
    public ResponseEntity<?> findDetail(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO, @PathVariable Long postId) {
        return postService.findDetail(userAccessDTO, postId);
    }


    @ApiOperation(value = "게시글 좋아요", notes = "게시글에 대해 좋아요 버튼을 누른다.\n\n" + "code: 200 조회 성공, 400 잘못된 postId 요청")
    @PostMapping("/{postId}/like")
    public ResponseEntity<?> likePost(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO, @PathVariable Long postId) {
        return postService.likePost(userAccessDTO, postId);
    }

    @ApiOperation(value = "게시글 싫어요", notes = "게시글에 대해 싫어요 버튼을 누른다.\n\n" + "code: 200 조회 성공, 400 잘못된 postId 요청")
    @PostMapping("/{postId}/dislike")
    public ResponseEntity<?> disLikePost(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO, @PathVariable Long postId) {
        return postService.dislikePost(userAccessDTO, postId);
    }

    @ApiOperation(value = "게시글 좋아요/싫어요 삭제", notes = "게시글에 대해 싫어요 버튼을 누른다.\n\n" + "code: 200 조회 성공, 400 잘못된 postId 요청")
    @DeleteMapping("/{postId}/cancel")
    public ResponseEntity<?> cancelLikePost(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO, @PathVariable Long postId) {
        return postService.cancelLikePost(userAccessDTO, postId);
    }

    @ApiOperation(value = "게시글 검색", notes = "키워드로 게시글을 검색한다.\n\n" + "code: 200 조회 성공, 204 조회 성공 + 표시할 내용 없음, 500 알 수 없는 서버 오류")
    @GetMapping("/searchPost")
    public ResponseEntity<?> searchPost(@RequestParam(required = true) String keyword,
                                        @RequestParam(required = false, defaultValue = "1") int page,
                                        @RequestParam("type")SearchType type) {
        return postService.findPostByKeywordAndType(keyword, page, type);
    }


}
