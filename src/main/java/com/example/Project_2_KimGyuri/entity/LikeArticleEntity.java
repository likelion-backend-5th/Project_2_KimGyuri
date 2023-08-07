package com.example.Project_2_KimGyuri.entity;

import com.example.Project_2_KimGyuri.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "like_article")
public class LikeArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private UserEntity usersId;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private ArticleEntity article;
}
