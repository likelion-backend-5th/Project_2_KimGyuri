package com.example.Project_2_KimGyuri.service;

import com.example.Project_2_KimGyuri.dto.CommentDto;
import com.example.Project_2_KimGyuri.entity.ArticleEntity;
import com.example.Project_2_KimGyuri.entity.CommentEntity;
import com.example.Project_2_KimGyuri.entity.user.UserEntity;
import com.example.Project_2_KimGyuri.exceptions.ArticleNotFoundException;
import com.example.Project_2_KimGyuri.exceptions.AuthorizationException;
import com.example.Project_2_KimGyuri.exceptions.CommentNotFoundException;
import com.example.Project_2_KimGyuri.exceptions.UserNotFoundException;
import com.example.Project_2_KimGyuri.jwt.JwtTokenUtils;
import com.example.Project_2_KimGyuri.repository.ArticleRepository;
import com.example.Project_2_KimGyuri.repository.CommentRepository;
import com.example.Project_2_KimGyuri.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final HttpServletRequest request;
    private final JwtTokenUtils jwtTokenUtils;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    //인증된 사용자 정보 추출
    private UserEntity getUserFromToken() {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.split(" ")[1];
            if (jwtTokenUtils.validate(token)) {
                String username = jwtTokenUtils.parseClaims(token).getSubject();
                Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
                if (optionalUser.isPresent()) {
                    return optionalUser.get();
                } else {
                    throw new UserNotFoundException();
                }
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); //유효하지 않은 토큰입니다
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); //토큰의 형식이 잘못되었습니다
        }
    }

    //댓글 등록
    public CommentDto createComment(Long articleId, CommentDto dto) {
        UserEntity user = getUserFromToken();

        Optional<ArticleEntity> optionalArticle = articleRepository.findById(articleId);
        if (optionalArticle.isEmpty() || (optionalArticle.get().getDeletedAt() != null))
            throw new ArticleNotFoundException();

        CommentEntity newComment = new CommentEntity();
        newComment.setUsersId(user);
        newComment.setArticle(optionalArticle.get());
        newComment.setContent(dto.getContent());
        newComment = commentRepository.save(newComment);
        return CommentDto.fromEntity(newComment);
    }

    //댓글 수정
    public CommentDto updateComment(Long articleId, Long commentId, CommentDto dto) {
        UserEntity user = getUserFromToken();

        Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty())
            throw new CommentNotFoundException();

        CommentEntity comment = optionalComment.get();

        Optional<ArticleEntity> optionalArticle = articleRepository.findById(articleId);
        if (optionalArticle.isEmpty() || (optionalArticle.get().getDeletedAt() != null))
            throw new ArticleNotFoundException();

        if(comment.getUsersId().getId().equals(user.getId())) {
            if(comment.getDeletedAt() != null || !comment.getArticle().getId().equals(articleId)) {
                throw new CommentNotFoundException();
            }
            comment.setContent(dto.getContent());
            commentRepository.save(comment);
            return CommentDto.fromEntity(comment);
        } else {
            throw new AuthorizationException();
        }
    }

    //댓글 삭제
    public void deleteComment(Long articleId, Long commentId) {
        UserEntity user = getUserFromToken();

        Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty())
            throw new CommentNotFoundException();

        CommentEntity comment = optionalComment.get();

        Optional<ArticleEntity> optionalArticle = articleRepository.findById(articleId);
        if (optionalArticle.isEmpty() || (optionalArticle.get().getDeletedAt() != null))
            throw new ArticleNotFoundException();

        if(comment.getUsersId().getId().equals(user.getId())) {
            if (comment.getDeletedAt() != null || !comment.getArticle().getId().equals(articleId)) {
                throw new CommentNotFoundException();
            }
            comment.setDeletedAt(new Date());
            commentRepository.save(comment);
        }
    }
}
