package com.example.cmproject.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ALL_READ("모든 사용자 읽기 권한"),
    MEMBER_READ("회원 사용자 읽기 권한"),
    ADMIN_WRITE("관리자 쓰기 권한, 모든 사용자 읽기 가능");

    private final String role;

    public String getRole() {
        return role;
    }

}
