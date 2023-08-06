package com.example.Project_2_KimGyuri.entity.user;

import com.example.Project_2_KimGyuri.entity.ArticleEntity;
import com.example.Project_2_KimGyuri.entity.CommentEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;

    @Column(name = "profile_img")
    private String profileImg;

    private String email;
    private String phone;

    @OneToMany(mappedBy = "usersId")
    private List<ArticleEntity> articles;

    @OneToMany(mappedBy = "usersId")
    private List<CommentEntity> comments;
}
