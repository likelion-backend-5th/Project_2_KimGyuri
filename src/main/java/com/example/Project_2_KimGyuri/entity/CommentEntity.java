package com.example.Project_2_KimGyuri.entity;

import com.example.Project_2_KimGyuri.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "comment")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private UserEntity usersId;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private ArticleEntity article;

    private String content;

    @Column(name = "deleted_at")
    private Date deletedAt;
}
