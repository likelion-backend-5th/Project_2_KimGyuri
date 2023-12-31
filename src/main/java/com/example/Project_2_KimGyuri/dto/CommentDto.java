package com.example.Project_2_KimGyuri.dto;

import com.example.Project_2_KimGyuri.entity.CommentEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentDto {
    private String username;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    public static CommentDto fromEntity(CommentEntity entity) {
        CommentDto dto = new CommentDto();
        dto.setUsername(entity.getUsersId().getUsername());
        dto.setContent(entity.getContent());
        return dto;
    }
}
