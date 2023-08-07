package com.example.Project_2_KimGyuri.dto;

import com.example.Project_2_KimGyuri.entity.ArticleEntity;
import com.example.Project_2_KimGyuri.entity.ArticleImagesEntity;
import com.example.Project_2_KimGyuri.entity.CommentEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OneArticleDto {
    private String username;
    private String title;
    private String content;
    private List<String> imageUrls;
    private List<CommentDto> comments;

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

        List<CommentDto> comments = new ArrayList<>();
        for (CommentEntity comment : entity.getComments()) {
            CommentDto commentDto = new CommentDto();
            commentDto.setUsername(comment.getUsersId().getUsername());
            commentDto.setContent(comment.getContent());
            comments.add(commentDto);
        }
        dto.setComments(comments);


        return dto;
    }
}
