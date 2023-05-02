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
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = LAZY)
    private User user;

    @JsonIgnore
    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = LAZY)
    private Post post;


    public PostLike(Post post, User user){
        this.post = post;
        this.user = user;
    }
}