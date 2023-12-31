package com.example.Project_2_KimGyuri.repository;

import com.example.Project_2_KimGyuri.entity.UserFriendsEntity;
import com.example.Project_2_KimGyuri.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface UserFriendsRepository extends JpaRepository<UserFriendsEntity, Long> {
    List<UserFriendsEntity> findAllByToUserIsAndAcceptedEquals(UserEntity user, boolean accepted);
    Optional<UserFriendsEntity> findByToUserIsAndFromUserIs(UserEntity toUser, UserEntity fromUser);
    Optional<UserFriendsEntity> findByToUserIsAndFromUserIsAndAcceptedEquals(UserEntity toUser, UserEntity fromUser, boolean accepted);
}
