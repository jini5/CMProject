package com.example.cmproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "post_like")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long PostLikeId;

    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = LAZY)
    private User user;

    @JsonIgnore
    @JoinColumn(name = "post_id", nullable = false)
    @ManyToOne(fetch = LAZY)
    private Post post;
    @JoinColumn(name = "isLiked")
    private boolean isLiked;


    public PostLike(Post post, User user, boolean isLiked){
        this.post = post;
        this.user = user;
        this.isLiked = isLiked;
    }

    public static PostLike PostLike(User user, Post post, boolean isLiked) {
        return PostLike.builder()
                .user(user)
                .post(post)
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