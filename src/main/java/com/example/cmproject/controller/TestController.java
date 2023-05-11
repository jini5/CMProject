package com.example.cmproject.controller;

import com.example.cmproject.dto.UserDTO;
import com.example.cmproject.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"테스트 서비스"}, description = "유저, 관리자 관련 테스트 (배포시 삭제)")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/test")
public class TestController {

    private final AdminService adminService;

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER')")
    /**유저 권한만 허용 **/
    @ApiOperation(value = "User 권한 확인", notes = "ROLE_USER 인 경우만 작동")
    public ResponseEntity<?> checkUser(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO) {
        return new ResponseEntity<>(userAccessDTO.getEmail(), HttpStatus.OK);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ApiOperation(value = "ADMIN 권한 확인", notes = "ROLE_ADMIN 인 경우만 작동")
    public ResponseEntity<?> checkAdmin(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO) {
        return new ResponseEntity<>(userAccessDTO.getEmail(), HttpStatus.OK);
    }

    @GetMapping("/role")
    @ApiOperation(value = "권한 확인", notes = "모든 권한에서 작동")
    public ResponseEntity<?> checkRole(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO) {
        return new ResponseEntity<>(userAccessDTO.getRole(), HttpStatus.OK);
    }

    @GetMapping("/selfAdmin")
    @ApiOperation(value = "개발 테스트용 관리자 승격", notes = "현재 사용자 관리자로 승격 (main 올릴땐 삭제)")
    public ResponseEntity<?> selfAdmin(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO) {
        return adminService.setUserToAdmin(userAccessDTO.getEmail());
    }

    @GetMapping("/UpdatePassword")
    @ApiOperation(value = "개발 테스트용 비밀번호 변경", notes = "비밀번호 변경")
    public ResponseEntity<?> selfAdmin(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO, @RequestBody UserDTO.UpdatePWReqDTO updatePWReqDTO) {
        return adminService.updatePw(userAccessDTO,updatePWReqDTO);
    }

}
