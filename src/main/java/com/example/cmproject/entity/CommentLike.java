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
    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = LAZY)
    private Comment comment;


    public CommentLike(Comment comment, User user){
        this.comment = comment;
        this.user = user;
    }
}