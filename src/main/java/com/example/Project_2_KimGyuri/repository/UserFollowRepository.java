package com.example.Project_2_KimGyuri.repository;

import com.example.Project_2_KimGyuri.entity.UserFollowsEntity;
import com.example.Project_2_KimGyuri.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserFollowRepository extends JpaRepository<UserFollowsEntity, Long> {
    Optional<UserFollowsEntity> findByFollowerAndFollowing(UserEntity follower, UserEntity following);
    List<UserFollowsEntity> findAllByFollower(UserEntity user);
}
