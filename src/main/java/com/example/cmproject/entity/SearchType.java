package com.example.cmproject.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum SearchType {
    TITLE("title", "포스트 제목으로 검색"),
    USER("user", "유저명으로 검색"),
    CONTENT("content", "포스트 내용으로 검색"),
    TITLE_CONTENT("title, content", "포스트 제목, 내용으로 검색"),
    TITLE_CONTENT_USER("title, content, user", "포스트 제목,내용, 유저명으로 검색");

    @Getter
    private final String type;

    @Getter
    private final String description;
}