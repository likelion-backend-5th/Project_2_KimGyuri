package com.example.Project_2_KimGyuri.dto;

import com.example.Project_2_KimGyuri.entity.ArticleEntity;
import lombok.Data;

@Data
public class UserArticleListDto {
    private String username;
    private String title;
    private String repImage;

    public static UserArticleListDto fromEntity(ArticleEntity entity) {
        UserArticleListDto dto = new UserArticleListDto();
        dto.setUsername(entity.getUsersId().getUsername());
        dto.setTitle(entity.getTitle());
        if (entity.getDraft() == false)
            dto.setRepImage("basic.jpg");
        else
            dto.setRepImage(entity.getArticleImages().get(0).getImageUrl());
        return dto;
    }
}
