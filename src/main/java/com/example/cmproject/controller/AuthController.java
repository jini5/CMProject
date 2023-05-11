package com.example.cmproject.controller;

import com.example.cmproject.dto.TokenDTO;
import com.example.cmproject.dto.UserDTO;
import com.example.cmproject.service.TokenService;
import com.example.cmproject.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


@Api(tags = {"인증 서비스"}, description = "회원가입,로그인, 회원탈퇴, 토큰 리프레시")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping("/signup")
    @ApiOperation(value = "회원가입 (토큰 X)", notes = "정보를 입력받아 회원가입을 진행하고 DB에 저장.\n\n code: 201 회원가입 성공, 400 email 이 존재하거나 두 패스워드가 불일치")
    public ResponseEntity<?> signUp(@RequestBody UserDTO.SignupReqDTO signupReqDTO) {
        return userService.signup(signupReqDTO);
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인 (토큰 X)", notes = "이메일과 패스워드를 입력받아 로그인이 가능 성공하면 토큰발급.\n\n code: 200 로그인 성공, 401 탈퇴한 회원, 404 없는회원 또는 비밀번호 불일치")
    public ResponseEntity<?> signIn(@RequestBody UserDTO.LoginReqDTO loginReqDTO) {
        return userService.login(loginReqDTO);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "로그아웃 (토큰 O)", notes = "버튼을 누르면 현재 로그인 토큰을 로그아웃 테이블에 저장한다.\n\n " +
            "다음 요청시에 현재 토큰과 요청이 오면 토큰 유효성 검사에 걸려서 로그인을 다시 요청하게 된다.\n\n" +
            "code: 200 로그아웃 성공, 401 이미 로그아웃한 토큰, 500 서버에러-재로그인 유도")
    public ResponseEntity<?> logout(@ApiIgnore @RequestHeader(name = "Authorization") String header, @RequestBody TokenDTO.RefreshTokenReqDTO refreshTokenReqDTO) {
        return tokenService.logout(header, refreshTokenReqDTO);
    }

    @PostMapping("/token/refresh")
    @ApiOperation(value = "토큰 리프레시", notes = "리프레시 토큰을 보내주면 유효성을 확인하고 엑세스토큰을 새로 발급.\n\n" +
            "code: 200 접근토큰 재생성 성공, 401 인증 만료로 인한 재로그인 필요")
    public ResponseEntity<?> validateRefreshToken(@RequestBody TokenDTO.RefreshTokenReqDTO refreshTokenReqDTO) {
        return tokenService.validateRefreshToken(refreshTokenReqDTO.getRefreshToken());
    }

    @GetMapping("/email/check")
    @ApiOperation(value = "이메일 중복 확인 API", notes = "이메일을 보내주면 이미 가입되어있는 회원인지 확인한다.\n\n" +
            "code: 200 사용 가능한 이메일, 400 사용 불가능한 이메일")
    public ResponseEntity<?> emailDuplicationCheck(@RequestParam String userEmail) {
        return userService.emailDuplicationCheck(userEmail);
    }


    @PostMapping("/find/password")
    @ApiOperation(value = "비밀번호 변경 메일", notes = "이메일을 작성 시 이메일로 비밀번호 변경 url 전송.\n\n" +
            "code: 200 이메일 전송됨, 400 없는 이메일")
    public ResponseEntity<?> sendPassword(@RequestParam String email) {
        return userService.sendPwEmail(email);
    }


//    @PostMapping("/")
//    @ApiOperation(value = "카카오 로그인", notes = "소셜 로그인 중 카카오를 사용한 회원가입, 로그인.\n\n" +
//            "code: 200 이메일 전송됨, 400 없는 이메일")
//    public ResponseEntity<?> kakaoLogin() {
//        return userService
//    }


}
