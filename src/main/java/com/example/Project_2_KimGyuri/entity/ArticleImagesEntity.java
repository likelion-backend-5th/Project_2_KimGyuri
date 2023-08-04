package com.example.Project_2_KimGyuri.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "article_images")
public class ArticleImagesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private ArticleEntity articleId;

    @Column(name = "image_url")
    private String imageUrl;
}
