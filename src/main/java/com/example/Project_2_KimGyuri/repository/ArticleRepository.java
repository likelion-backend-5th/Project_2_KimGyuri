package com.example.Project_2_KimGyuri.repository;

import com.example.Project_2_KimGyuri.entity.ArticleEntity;
import com.example.Project_2_KimGyuri.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {
    Page<ArticleEntity> findAllByUsersId_UsernameAndDeletedAtIsNull(String username, Pageable pageable);
    Page<ArticleEntity> findAllByUsersIdInAndDeletedAtIsNull(List<UserEntity> following, Pageable pageable);
}
