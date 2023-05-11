package com.example.cmproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "comment_like")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long CommentLikeId;

    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = LAZY)
    private User user;
    @JsonIgnore
    @JoinColumn(name = "comment_id")
    @ManyToOne(fetch = LAZY)
    private Comment comment;

    @JoinColumn(name = "isLiked")
    private boolean isLiked;


    public CommentLike(Comment comment, User user, boolean isLiked){
        this.comment = comment;
        this.user = user;
        this.isLiked = isLiked;
    }

    public static CommentLike CommentLike(User user, Comment comment, boolean isLiked) {
        return CommentLike.builder()
                .user(user)
                .comment(comment)
                .isLiked(isLiked)
                .build();
    }

    public boolean getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }
}