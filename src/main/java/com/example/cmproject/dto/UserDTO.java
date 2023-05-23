package com.example.cmproject.dto;

import com.example.cmproject.entity.User;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

public class UserDTO {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ApiModel(value = "로그인")
    public static class LoginReqDTO {

        @ApiModelProperty(value = "이메일 ", required = true)
        private String userEmail;

        @ApiModelProperty(value = "비밀번호 ", required = true)
        private String userPassword;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ApiModel(value = "토큰에 담길 정보")
    public static class UserAccessDTO {
        @ApiModelProperty(value = "이메일 ", required = true)
        private String email;

        @ApiModelProperty(value = "권한 ", required = true)
        private String role;

        public UserAccessDTO(Claims claims) {
            this.email = claims.get("email", String.class);
            this.role = claims.get("role", String.class);
        }

        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Collections.singleton(new SimpleGrantedAuthority(this.role));
        }

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    @ApiModel(value = "회원가입")
    public static class SignupReqDTO {
        @ApiModelProperty(value = "닉네임", required = true)
        private String nickName;
        @ApiModelProperty(value = "이메일", required = true)
        private String userEmail;
        @ApiModelProperty(value = "비밀번호", required = true)
        private String userPassword;
        @ApiModelProperty(value = "비밀번호 확인", required = true)
        private String passwordConfirmation;
        @ApiModelProperty(value = "프로필이미지")
        private String profileImage;
        @ApiModelProperty(value = "전화번호")
        private String userPhoneNumber;
        @ApiModelProperty(value = "생년월일")
        private String userBirthday;


        public User toEntity() {

            return User.builder()
                    .nickName(nickName)
                    .email(userEmail)
                    .profileImage(profileImage)
                    .password(userPassword)
                    .phoneNumber(userPhoneNumber)
                    .birthday(userBirthday)
                    .role("ROLE_USER")
                    .deleteCheck("available")
                    .build();
        }

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @ApiModel(value = "회원정보수정 입력")
    public static class UpdateUserReqDTO {

        @ApiModelProperty(value = "기존 비밀번호", required = true)
        private String userPassword;
        @ApiModelProperty(value = "새로운 비밀번호")
        private String changePassword;
        @ApiModelProperty(value = "새로운 비밀번호 재입력")
        private String passwordConfirmation;
        @ApiModelProperty(value = "프로필이미지")
        private String profileImage;
        @ApiModelProperty(value = "닉네임", required = true)
        private String userNickName;
        @ApiModelProperty(value = "전화번호")
        private String userPhoneNumber;
        @ApiModelProperty(value = "생년월일")
        private String userBirthday;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @ApiModel(value = "회원정보수정 출력")
    public static class UpdateUserResDTO {

        @ApiModelProperty(value = "닉네임", required = true)
        private String userNickName;
        @ApiModelProperty(value = "이메일", required = true)
        private String userEmail;
        @ApiModelProperty(value = "프로필이미지")
        private String userProfileImage;
        @ApiModelProperty(value = "전화번호")
        private String userPhoneNumber;
        @ApiModelProperty(value = "생년월일")
        private String userBirthday;



        public UpdateUserResDTO(User user) {
            this.userNickName = user.getNickName();
            this.userEmail = user.getEmail();
            this.userBirthday = user.getBirthday();
            this.userPhoneNumber = user.getPhoneNumber();
            this.userProfileImage = user.getProfileImage();

        }
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @ApiModel(value = "회원 탈퇴")
    public static class DeleteUserReqDTO {
        @ApiModelProperty(value = "비밀번호 ", required = true)
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @ApiModel(value = "이메일만 사용하는 dto")
    public static class EmailOnly {
        @ApiModelProperty(value = "이메일 ", required = true)
        private String email;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @ApiModel(value = "회원리스트 출력")
    public static class UserListDto {
        private Long userId;
        private String userEmail;
        private String userNickName;
        private String userRole;

        public UserListDto(User user) {
            this.userId = user.getUserId();
            this.userEmail = user.getEmail();
            this.userNickName = user.getNickName();
            this.userRole = user.getRole();
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class UserDetailsForAdmin {
        private Long userId;
        private String userName;
        private String userEmail;
        private String userPhoneNumber;
        private String userProfileImage;
        private String userBirthday;
        private String userRole;
        private LocalDateTime createdDate;
        private LocalDateTime updatedDate;
        private String deleteCheck;

        public UserDetailsForAdmin(User user) {
            this.userId = user.getUserId();
            this.userName = user.getNickName();
            this.userEmail = user.getEmail();
            this.userProfileImage = user.getProfileImage();
            this.userPhoneNumber = user.getPhoneNumber();
            this.userBirthday = user.getBirthday();
            this.userRole = user.getRole();
            this.createdDate = user.getCreatedDate();
            this.updatedDate = user.getUpdatedDate();
            this.deleteCheck = user.getDeleteCheck();
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @ApiModel(value = "관리자의 회원정보수정 입력", description = "변경 할 값만 입력")
    public static class PatchUserByAdminReqDTO {
        @ApiModelProperty(value = "사용자 닉네임 입력.")
        private String userNickName;
        @ApiModelProperty(value = "사용자 전화번호 입력")
        private String userPhoneNumber;
        @ApiModelProperty(value = "사용자 생년월일 입력")
        private String userBirthday;
        @ApiModelProperty(value = "사용자 프로필 입력")
        private String userProfileImage;
        @ApiModelProperty(value = "사용자 ROLE")
        private String userRole;
        @ApiModelProperty(value = "사용자 탈퇴여부 입력\n\n available or withdraw")
        private String deleteCheck;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @ApiModel(value = "개발용 비밀번호 변경")
    public static class UpdatePWReqDTO {
        @ApiModelProperty(value = "새롭게 변경할 비밀번호")
        private String changePassword;
    }
}

