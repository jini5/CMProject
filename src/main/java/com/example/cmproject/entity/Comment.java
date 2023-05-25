package com.example.cmproject.entity;

import com.example.cmproject.dto.CommentDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@Table(name = "comment")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "depth", nullable = false)
    private int depth;
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<CommentLike> commentLikes = new ArrayList<>(); // 댓글 좋아요

    @Column(name = "created_time",nullable = false)
    private LocalDateTime createdTime;

    @Column(name = "updated_time",nullable = false)
    private LocalDateTime updatedTime;

    @Column(name = "delete_check")
    private boolean deleteCheck;



    public void update(CommentDTO.UpdateCommentReqDTO updateCommentReqDTO) {
        this.content = updateCommentReqDTO.getContent();
        this.updatedTime = LocalDateTime.now();
    }

}
