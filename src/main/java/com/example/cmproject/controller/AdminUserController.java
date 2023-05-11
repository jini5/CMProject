package com.example.cmproject.controller;


import com.example.cmproject.dto.UserDTO;
import com.example.cmproject.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"관리자의 사용자 관리 서비스"}, description = "관리자 권한 부여, 박탈, 회원 리스트 조회, 회원 상세정보 조회")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/admin")
public class AdminUserController {

    private final AdminService adminService;

    @PatchMapping("/setAdmin/{email}")
    @ApiOperation(value = "관리자 권한 부여", notes = "입력받은 email 을 통해 관리자 권한 부여.\n\n code: 200 성공, 404 해당 이메일을 찾지 못함")
    public ResponseEntity<?> setAdmin(@PathVariable String email) {
        return adminService.setUserToAdmin(email);
    }

    @PatchMapping("/deprivation/{email}")
    @ApiOperation(value = "관리자 권한 박탈", notes = "입력받은 email 을 통해 관리자 권한 박탈.\n\n code: 200 성공, 404 해당 이메일을 찾지 못함")
    public ResponseEntity<?> setUser(@PathVariable String email) {
        return adminService.setAdminToUser(email);
    }

    @GetMapping("/userList")
    @ApiOperation(value = "회원 리스트 조회", notes = "관리자가 회원 목록 20명씩 조회.\n\n code: 200 조회 성공, 204 표시할 내용 없음, 500 서버에러")
    public ResponseEntity<?> findUserList(@RequestParam(required = false, defaultValue = "1") int page) {
        return adminService.findUserList(page);
    }

    @GetMapping("/user/{userId}")
    @ApiOperation(value = "회원 상세 정보 조회", notes = "관리자가 회원 상세 정보 조회.\n\n code: 200 조회 성공, 404 해당 사용자 없음")
    public ResponseEntity<?> findUser(@PathVariable Long userId) {
        return adminService.findUser(userId);
    }

    @PutMapping("/detail/{userId}")
    @ApiOperation(value = "회원 상세 정보 수정", notes = "관리자가 회원 상세 정보 수정.\n\n code: 200 수정 성공, 404 해당 사용자 없음,400 해당 사용자 타입 없음.")
    public ResponseEntity<?> updateUserInfo(@PathVariable Long userId, @RequestBody UserDTO.PatchUserByAdminReqDTO dto) {
        return adminService.updateUserInfo(userId, dto);
    }

}
