package com.example.Project_2_KimGyuri.controller;

import com.example.Project_2_KimGyuri.dto.ResponseDto;
import com.example.Project_2_KimGyuri.entity.user.CustomUserDetails;
import com.example.Project_2_KimGyuri.jwt.JwtTokenUtils;
import com.example.Project_2_KimGyuri.jwt.dto.JwtRequestDto;
import com.example.Project_2_KimGyuri.jwt.dto.JwtTokenDto;
import com.example.Project_2_KimGyuri.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationService service;

    //회원가입
    @PostMapping("/signup")
    public ResponseDto signup(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("password-check") String passwordCheck,
                              @RequestParam("email") String email, @RequestParam("phone")String phone) {
        ResponseDto response = new ResponseDto();
        if (username.isEmpty() || password.isEmpty()) {
            response.setMessage("아이디와 비밀번호는 필수 입력 항목입니다.");
            return response;
        }

        if (manager.userExists(username)) {
            response.setMessage("이미 존재하는 사용자 아이디입니다.");
            return response;
        }

        if(password.equals(passwordCheck)) {
            manager.createUser(CustomUserDetails.builder().
                    username(username).
                    password(passwordEncoder.encode(password))
                    .profileImg("")
                    .email(email)
                    .phone(phone)
                    .build());
            response.setMessage("회원가입이 완료되었습니다.");
            return response;
        }

        response.setMessage("비밀번호가 일치하지 않습니다.");
        return response;
    }

    //로그인
    @PostMapping("/login")
    public JwtTokenDto loginJwt(@RequestBody JwtRequestDto dto) {
        UserDetails userDetails = manager.loadUserByUsername(dto.getUsername());
        if(!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        JwtTokenDto response = new JwtTokenDto();
        response.setToken(jwtTokenUtils.generateToken(userDetails));
        return response;
    }

    //프로필 사진 업로드
    @PutMapping(value = "/profileImg", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto updateProfile(@RequestParam("profileImg")MultipartFile profileImg) {
        service.updateImage(profileImg);
        ResponseDto response = new ResponseDto();
        response.setMessage("프로필 사진이 업로드 되었습니다.");
        return response;
    }
}
