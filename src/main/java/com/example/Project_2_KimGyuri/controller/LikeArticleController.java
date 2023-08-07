package com.example.Project_2_KimGyuri.controller;

import com.example.Project_2_KimGyuri.dto.ResponseDto;
import com.example.Project_2_KimGyuri.service.LikeArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/feed/{articleId}/like")
public class LikeArticleController {
    private final LikeArticleService service;

    @PostMapping
    public ResponseDto like(@PathVariable("articleId") Long articleId) {
        String result = service.LikeArticle(articleId);
        ResponseDto response = new ResponseDto();
        if (result.equals("like")) {
            response.setMessage("좋아요");
            return response;
        }
        else {
            response.setMessage("좋아요 취소");
            return response;
        }
    }
}
