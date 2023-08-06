package com.example.Project_2_KimGyuri.repository;

import com.example.Project_2_KimGyuri.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}
