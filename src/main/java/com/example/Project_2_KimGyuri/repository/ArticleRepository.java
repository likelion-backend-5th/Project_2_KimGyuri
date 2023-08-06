package com.example.Project_2_KimGyuri.repository;

import com.example.Project_2_KimGyuri.entity.ArticleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {
    Page<ArticleEntity> findAllByUsersId_UsernameAndAndDeletedAtIsNull(String username, Pageable pageable);
}
