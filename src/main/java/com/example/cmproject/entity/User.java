package com.example.cmproject.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "profile_image", nullable = false)
    private String profileImage;

    @Column(name = "nick_name", nullable = false)
    private String nickName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "birthday", nullable = false)
    private String birthday;

    @Column(name = "role", nullable = false)
    private String role;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "delete_check")
    private String deleteCheck;

    public void updatePassword(String password) {
        this.password = password;
    }

    @Builder
    public User(String email, String password,String profileImage, String nickName, String birthday, String phoneNumber, String role, String deleteCheck) {
        this.email = email;
        this.password = password;
        this.profileImage=profileImage;
        this.nickName = nickName;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.deleteCheck = deleteCheck;
    }

    public void update(String changePassword,String profileImage, String nickName, String birthday, String phoneNumber) {
        this.password = changePassword;
        this.profileImage=profileImage;
        this.nickName = nickName;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
    }

}
