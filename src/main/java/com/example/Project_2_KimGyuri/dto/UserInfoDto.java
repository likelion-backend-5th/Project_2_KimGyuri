package com.example.Project_2_KimGyuri.dto;

import com.example.Project_2_KimGyuri.entity.user.UserEntity;
import lombok.Data;

@Data
public class UserInfoDto {
    private String username;
    private String profile;

    public static UserInfoDto fromEntity(UserEntity entity) {
        UserInfoDto dto = new UserInfoDto();
        dto.setUsername(entity.getUsername());
        dto.setProfile(entity.getProfileImg());
        return dto;
    }
}
