package com.example.Project_2_KimGyuri.service;

import com.example.Project_2_KimGyuri.dto.UserInfoDto;
import com.example.Project_2_KimGyuri.entity.UserFollowsEntity;
import com.example.Project_2_KimGyuri.entity.user.UserEntity;
import com.example.Project_2_KimGyuri.jwt.JwtTokenUtils;
import com.example.Project_2_KimGyuri.repository.UserFollowRepository;
import com.example.Project_2_KimGyuri.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final HttpServletRequest request;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserRepository userRepository;
    private final UserFollowRepository userFollowRepository;

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

    //사용자 정보 조회
    public UserInfoDto readUser(String username) {
        UserEntity user = getUserFromToken();

        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //사용자를 찾을 수 없습니다.

        UserEntity userInfo = optionalUser.get();
        UserInfoDto dto = new UserInfoDto();
        dto.setUsername(username);

        if (userInfo.getProfileImg().isBlank())
            dto.setProfile("basicProfile.jpg");
        else
            dto.setProfile(userInfo.getProfileImg());
        return dto;
    }

    //팔로우/팔로우해제
    public String followUser(String username) {
        UserEntity loginUser = getUserFromToken();

        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //사용자를 찾을 수 없습니다.

        UserEntity user = optionalUser.get();
        if (loginUser.getId().equals(user.getId()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); //본인 팔로우 불가. 권한이 없습니다.

        Optional<UserFollowsEntity> optionalUserFollows = userFollowRepository.findByFollower_Id(user.getId());
        //팔로우
        if (optionalUserFollows.isEmpty()) {
            UserFollowsEntity userFollows = new UserFollowsEntity();
            userFollows.setFollower(user);
            userFollows.setFollowing(loginUser);
            userFollowRepository.save(userFollows);
            return "follow";
        }
        //팔로우 해제
        else {
            UserFollowsEntity userFollows = optionalUserFollows.get();
            userFollowRepository.deleteById(userFollows.getId());
            return "cancel";
        }
    }
}
