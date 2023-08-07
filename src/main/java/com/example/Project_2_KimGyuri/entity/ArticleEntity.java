package com.example.Project_2_KimGyuri.entity;

import com.example.Project_2_KimGyuri.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "article")
public class ArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private UserEntity usersId;

    private String title;
    private String content;
    private Boolean draft;

    @Column(name = "deleted_at")
    private Date deletedAt;

    @OneToMany(mappedBy = "articleId", cascade = CascadeType.ALL)
    private List<ArticleImagesEntity> articleImages;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<CommentEntity> comments;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<LikeArticleEntity> likes;
}
