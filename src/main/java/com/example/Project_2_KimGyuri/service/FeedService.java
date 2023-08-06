package com.example.Project_2_KimGyuri.service;

import com.example.Project_2_KimGyuri.dto.ArticleDto;
import com.example.Project_2_KimGyuri.dto.OneArticleDto;
import com.example.Project_2_KimGyuri.dto.UserArticleListDto;
import com.example.Project_2_KimGyuri.entity.ArticleEntity;
import com.example.Project_2_KimGyuri.entity.ArticleImagesEntity;
import com.example.Project_2_KimGyuri.entity.user.UserEntity;
import com.example.Project_2_KimGyuri.jwt.JwtTokenUtils;
import com.example.Project_2_KimGyuri.repository.ArticleImagesRepository;
import com.example.Project_2_KimGyuri.repository.ArticleRepository;
import com.example.Project_2_KimGyuri.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedService {
    private final ArticleRepository articleRepository;
    private final ArticleImagesRepository articleImagesRepository;
    private final HttpServletRequest request;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserRepository userRepository;

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

    //피드 작성
    public ArticleDto createArticle(String title, String content, MultipartFile[] images) {
        UserEntity user = getUserFromToken();

        ArticleEntity newArticle = new ArticleEntity();
        newArticle.setTitle(title);
        newArticle.setContent(content);
        newArticle.setUsersId(user);
        newArticle.setDraft(false);

        Optional<MultipartFile[]> optionalImages = Optional.ofNullable(images);
        if (optionalImages.isPresent()) {
            newArticle.setDraft(true);
            articleRepository.save(newArticle);
            for (MultipartFile image : images) {
                String imageDir = String.format("images/%d/", user.getId());
                try {
                    Files.createDirectories(Path.of(imageDir));
                } catch (IOException e) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
                }

                String originalFilename = image.getOriginalFilename();
                UUID uuid = UUID.randomUUID();
                String imageFilename = uuid + "_" +originalFilename;
                String imagePath = imageDir + imageFilename;

                try {
                    image.transferTo(Path.of(imagePath));
                } catch (IOException e) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                ArticleImagesEntity newImages = new ArticleImagesEntity();
                newImages.setArticleId(newArticle);
                newImages.setImageUrl(String.format("/images/%d/%s", user.getId(), imageFilename));
                articleImagesRepository.save(newImages);
            }
        }
        return ArticleDto.fromEntity(articleRepository.save(newArticle));
    }

    //사용자 피드 조회
    public Page<UserArticleListDto> readArticleAll(String username, Integer page) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //사용자를 찾을 수 없습니다.
        }
        UserEntity user = optionalUser.get();
        Pageable pageable = PageRequest.of(page, 20, Sort.by("id"));
        Page<ArticleEntity> articleEntityPage = articleRepository.findAllByUsersId_Username(user.getUsername(), pageable);
        return articleEntityPage.map(UserArticleListDto::fromEntity);
    }

    //피드 단독 조회
    public OneArticleDto readArticleOne(Long articleId) {
        UserEntity user = getUserFromToken();

        Optional<ArticleEntity> optionalArticle = articleRepository.findById(articleId);
        if (optionalArticle.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //피드를 찾을 수 없습니다.
        return OneArticleDto.fromEntity(optionalArticle.get());
    }
}
