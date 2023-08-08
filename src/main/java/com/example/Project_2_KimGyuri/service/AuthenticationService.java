package com.example.Project_2_KimGyuri.service;

import com.example.Project_2_KimGyuri.entity.user.CustomUserDetails;
import com.example.Project_2_KimGyuri.entity.user.UserEntity;
import com.example.Project_2_KimGyuri.exceptions.ImageUploadException;
import com.example.Project_2_KimGyuri.exceptions.UserNotFoundException;
import com.example.Project_2_KimGyuri.jwt.JwtTokenUtils;
import com.example.Project_2_KimGyuri.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsManager {
    private final UserRepository userRepository;
    private final HttpServletRequest request;
    private final JwtTokenUtils jwtTokenUtils;

    //회원가입
    @Override
    public void createUser(UserDetails user) {
        if (this.userExists(user.getUsername())) {
            log.warn("이미 존재하는 사용자 아이디입니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //이미 존재하는 사용자 아이디
        }
        try {
            userRepository.save(((CustomUserDetails) user).newEntity());
        } catch (ClassCastException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR); //서버 에러
        }
    }

    //회원정보 수정
    @Override
    public void updateUser(UserDetails user) {
    }

    //회원 탈퇴
    @Override
    public void deleteUser(String username) {

    }

    //비밀번호 변경
    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    //이미 존재하는 아이디인지 확인
    @Override
    public boolean userExists(String username) {
        return this.userRepository.existsByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException(username);
        return CustomUserDetails.fromEntity(optionalUser.get());
    }

    public void updateImage(MultipartFile image) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.split(" ")[1];
            if (jwtTokenUtils.validate(token)) {
                String username = jwtTokenUtils.parseClaims(token).getSubject();
                Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
                if (optionalUser.isPresent()) {
                    UserEntity user = optionalUser.get();

                    String profileDir = String.format("profile/%d/", user.getId());
                    try {
                        Files.createDirectories(Path.of(profileDir));
                    } catch (IOException e) {
                        throw new ImageUploadException();
                    }

                    String originalFilename = image.getOriginalFilename();
                    String[] fileNameSplit = originalFilename.split("\\.");
                    String extension = fileNameSplit[fileNameSplit.length - 1];
                    String profileFilename = "profile." + extension;

                    String profilePath = profileDir + profileFilename;

                    try {
                        image.transferTo(Path.of(profilePath));
                    } catch (IOException e) {
                        throw new ImageUploadException();
                    }
                    user.setProfileImg(String.format("/static/%d/%s", user.getId(), profileFilename));
                    userRepository.save(user);
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
}
