package com.example.cmproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

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
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "to_user_id")
    private User parentUser;

    @OneToMany(mappedBy = "parentComment")
    @OrderBy("createdAt asc")
    private List<Comment> children = new ArrayList<>();

    private String content;

    private boolean isDeleted;

    public void setParentReply(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public void setParentUser(User parentUser) {
        this.parentUser = parentUser;
    }

    public static Comment createReply(Post post, User user, String content) {
        return Comment.builder()
                .post(post)
                .user(user)
                .content(content).build();
    }

    public void updateReply(String content) {
        this.content = content;
    }

    public void changeDeleteStatus() {
        this.isDeleted = true;
    }
}
