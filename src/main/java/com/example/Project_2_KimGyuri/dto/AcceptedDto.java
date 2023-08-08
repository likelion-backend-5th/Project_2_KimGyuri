package com.example.Project_2_KimGyuri.dto;

import com.example.Project_2_KimGyuri.entity.UserFriendsEntity;
import lombok.Data;

@Data
public class AcceptedDto {
    private boolean accepted;

    public static AcceptedDto fromEntity(UserFriendsEntity entity) {
        AcceptedDto dto = new AcceptedDto();
        dto.setAccepted(entity.isAccepted());
        return dto;
    }
}
