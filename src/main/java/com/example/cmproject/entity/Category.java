package com.example.cmproject.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Table(name = "category")
public class Category {

    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    private Category parent;

    @Column(name = "category_depth")
    private int categoryDepth;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @OneToMany(mappedBy = "parent")
    private List<Category> children = new ArrayList<>();

    public void update(String name, Role role){
        this.name = name;
        this.role = role;
    }

    public Category(String name, int categoryDepth, Role role) {
        this.name = name;
        this.categoryDepth = categoryDepth;
        this.role =role;
    }

    public Category(String categoryName, Category categoryParent, int categoryDepth, Role role) {
        this.name = categoryName;
        this.parent = categoryParent;
        this.categoryDepth = categoryDepth;
        this.role = role;
    }

    public Role getRole() {
        return role;
    }
}