package com.example.Project_2_KimGyuri.repository;

import com.example.Project_2_KimGyuri.entity.ArticleImagesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleImagesRepository extends JpaRepository<ArticleImagesEntity, Long> {
    List<ArticleImagesEntity> findByArticleId_Id(Long articleId);
}