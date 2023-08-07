package com.example.Project_2_KimGyuri.dto;

import com.example.Project_2_KimGyuri.entity.UserFriendsEntity;
import lombok.Data;

@Data
public class RequestFriendDto {
    private String fromUsername;
    public static RequestFriendDto fromEntity(UserFriendsEntity entity) {
        RequestFriendDto dto = new RequestFriendDto();
        dto.setFromUsername(entity.getFromUser().getUsername());
        return dto;
    }
}
