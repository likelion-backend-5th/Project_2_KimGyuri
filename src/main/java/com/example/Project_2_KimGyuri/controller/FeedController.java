package com.example.Project_2_KimGyuri.controller;

import com.example.Project_2_KimGyuri.dto.OneArticleDto;
import com.example.Project_2_KimGyuri.dto.ResponseDto;
import com.example.Project_2_KimGyuri.dto.UserArticleListDto;
import com.example.Project_2_KimGyuri.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/feed")
public class FeedController {
    private final FeedService service;

    //피드 등록
    @PostMapping("/create")
    public ResponseDto create(@RequestParam("title") String title, @RequestParam("content") String content, @RequestParam(value = "images", required = false)MultipartFile[] images) {
        service.createArticle(title, content, images);
        ResponseDto response = new ResponseDto();
        response.setMessage("피드가 등록되었습니다.");
        return response;
    }

    //사용자 피드 조회
    @GetMapping("/{username}")
    public Page<UserArticleListDto> readAll(@PathVariable("username") String username, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        return service.readArticleAll(username, page);
    }

    //피드 단독 조회
    @GetMapping("/read/{articleId}")
    public OneArticleDto readOne(@PathVariable("articleId") Long articleId) {
        return service.readArticleOne(articleId);
    }

    //피드 수정
    @PutMapping("/update/{articleId}")
    public ResponseDto update(@PathVariable("articleId") Long articleId,
                              @RequestParam(value = "title", required = false) String title, @RequestParam(value = "content", required = false) String content,
                              @RequestParam(value = "images", required = false)MultipartFile[] images, @RequestParam(value = "deleteImageId", required = false) List<Long> deleteImageId) {
        service.updateArticle(articleId, title, content, images, deleteImageId);
        ResponseDto response = new ResponseDto();
        response.setMessage("피드가 수정되었습니다.");
        return response;
    }
}
