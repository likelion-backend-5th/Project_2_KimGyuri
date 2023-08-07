package com.example.Project_2_KimGyuri.repository;

import com.example.Project_2_KimGyuri.entity.LikeArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeArticleRepository extends JpaRepository<LikeArticleEntity, Long> {
    Optional<LikeArticleEntity> findByArticle_IdAndUsersId_Id(Long articleId, Long userId);
    Optional<LikeArticleEntity> findAllByArticle_Id(Long articleId);
}
