package com.example.Project_2_KimGyuri.controller;

import com.example.Project_2_KimGyuri.dto.ResponseDto;
import com.example.Project_2_KimGyuri.entity.user.CustomUserDetails;
import com.example.Project_2_KimGyuri.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;

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
}
