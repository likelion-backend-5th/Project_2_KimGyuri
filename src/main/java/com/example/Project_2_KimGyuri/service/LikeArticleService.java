package com.example.Project_2_KimGyuri.service;

import com.example.Project_2_KimGyuri.entity.ArticleEntity;
import com.example.Project_2_KimGyuri.entity.LikeArticleEntity;
import com.example.Project_2_KimGyuri.entity.user.UserEntity;
import com.example.Project_2_KimGyuri.jwt.JwtTokenUtils;
import com.example.Project_2_KimGyuri.repository.ArticleRepository;
import com.example.Project_2_KimGyuri.repository.LikeArticleRepository;
import com.example.Project_2_KimGyuri.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeArticleService {
    private final ArticleRepository articleRepository;
    private final HttpServletRequest request;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserRepository userRepository;
    private final LikeArticleRepository likeArticleRepository;

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
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND); //사용자를 찾을 수 없습니다.
                }
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); //유효하지 않은 토큰입니다
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); //토큰의 형식이 잘못되었습니다
        }
    }

    //좋아요
    public String LikeArticle(Long articleId) {
        UserEntity user = getUserFromToken();

        Optional<ArticleEntity> optionalArticle = articleRepository.findById(articleId);
        if (optionalArticle.isEmpty() || (optionalArticle.get().getDeletedAt() != null))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //피드를 찾을 수 없습니다

        ArticleEntity article = optionalArticle.get();
        if (user.getId().equals(article.getUsersId().getId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); //본인 게시글 좋아요 불가. 권한이 없습니다.
        }

        Optional<LikeArticleEntity> optionalLikeArticle = likeArticleRepository.findByArticle_IdAndUsersId_Id(articleId, user.getId());
        //좋아요
        if (optionalLikeArticle.isEmpty()) {
            LikeArticleEntity like = new LikeArticleEntity();
            like.setUsersId(user);
            like.setArticle(article);
            likeArticleRepository.save(like);
            return "like";
        }
        //좋아요 취소
        else {
            LikeArticleEntity like = optionalLikeArticle.get();
            likeArticleRepository.deleteById(like.getId());
            return "cancel";
        }
    }
}
