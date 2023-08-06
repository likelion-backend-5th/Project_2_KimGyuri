package com.example.Project_2_KimGyuri.controller;

import com.example.Project_2_KimGyuri.dto.CommentDto;
import com.example.Project_2_KimGyuri.dto.ResponseDto;
import com.example.Project_2_KimGyuri.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("feed/{articleId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService service;

    //댓글 등록
    @PostMapping
    public ResponseDto create(@PathVariable("articleId") Long articleId, @RequestBody CommentDto dto) {
        service.createComment(articleId, dto);
        ResponseDto response = new ResponseDto();
        response.setMessage("댓글이 작성되었습니다.");
        return response;
    }

    //댓글 수정
    @PutMapping("/{commentId}")
    public ResponseDto update(@PathVariable("articleId") Long articleId, @PathVariable("commentId") Long commentId, @RequestBody CommentDto dto) {
        service.updateComment(articleId, commentId, dto);
        ResponseDto response = new ResponseDto();
        response.setMessage("댓글이 수정되었습니다.");
        return response;
    }

    //댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseDto delete(@PathVariable("articleId") Long articleId, @PathVariable("commentId") Long commentId) {
        service.deleteComment(articleId, commentId);
        ResponseDto response = new ResponseDto();
        response.setMessage("댓글이 삭제되었습니다.");
        return response;
    }
}
