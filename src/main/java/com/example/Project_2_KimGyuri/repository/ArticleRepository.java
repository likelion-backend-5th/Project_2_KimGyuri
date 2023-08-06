package com.example.Project_2_KimGyuri.repository;

import com.example.Project_2_KimGyuri.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {
}
