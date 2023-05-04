package com.example.cmproject.entity;

import com.example.cmproject.dto.CommentDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long CommentId;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Boolean likeStatus = false;

    @Column(name = "LIKE_COUNT")// 좋아요갯수
    private Integer likeCount = 0;

    @Column(name = "content", nullable = false)
    private String content;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<CommentLike> commentLikes = new ArrayList<>(); // 댓글 좋아요

    @Column(name = "created_time",nullable = false)
    private LocalDateTime createdTime;

    @Column(name = "updated_time",nullable = false)
    private LocalDateTime updatedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "parent_id")
    private Comment parent;         // 부모 댓글

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>(); // 자식 댓글

    public void update(CommentDTO.CommentRequestDTO commentRequestDTO) {
        this.content = commentRequestDTO.getContent();
    }

    public void updateParent(Comment parent){
        this.parent = parent;
    }

    public boolean validateUser(User user) {
        return !this.user.equals(user);
    }
}
