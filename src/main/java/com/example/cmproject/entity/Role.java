package com.example.cmproject.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ALL_READ("all:read"),
    MEMBER_READ("member:read"),
    ADMIN_WRITE("admin:write");

    private final String role;

    public String getRole() {
        return role;
    }

}
