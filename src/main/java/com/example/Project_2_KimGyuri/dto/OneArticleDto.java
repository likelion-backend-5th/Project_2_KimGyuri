package com.example.Project_2_KimGyuri.dto;

import com.example.Project_2_KimGyuri.entity.ArticleEntity;
import com.example.Project_2_KimGyuri.entity.ArticleImagesEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OneArticleDto {
    private String username;
    private String title;
    private String content;
    private List<String> imageUrls;

    public static OneArticleDto fromEntity(ArticleEntity entity) {
        OneArticleDto dto = new OneArticleDto();
        dto.setUsername(entity.getUsersId().getUsername());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        List<String> imageUrls = new ArrayList<>();
        for (ArticleImagesEntity image : entity.getArticleImages()) {
            imageUrls.add(image.getImageUrl());
        }
        dto.setImageUrls(imageUrls);

        return dto;
    }
}
