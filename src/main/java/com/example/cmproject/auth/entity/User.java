package com.example.cmproject.auth.entity;

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

    @Column(name = "profile", nullable = false)
    private String profile;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "birthday", nullable = false)
    private String birthday;

    @Column(name = "sns")
    private String sns;
    @Column(name = "delete_check")
    private String deleteCheck;

    @Column(name = "role", nullable = false)
    private String role;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    public void updatePassword(String password) {
        this.password = password;
    }

    @Builder
    public User(String email, String password,String profile, String name, String birthday, String phoneNumber, String role, String sns, String deleteCheck) {
        this.email = email;
        this.password = password;
        this.profile=profile;
        this.name = name;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.sns = sns;
        this.deleteCheck = deleteCheck;
    }

    public void update(String changePassword,String profile, String name, String birthday, String phoneNumber) {
        this.password = changePassword;
        this.profile=profile;
        this.name = name;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
    }

}
