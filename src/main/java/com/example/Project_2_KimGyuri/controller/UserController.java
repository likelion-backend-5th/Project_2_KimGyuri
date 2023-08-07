package com.example.Project_2_KimGyuri.controller;

import com.example.Project_2_KimGyuri.dto.ResponseDto;
import com.example.Project_2_KimGyuri.dto.UserInfoDto;
import com.example.Project_2_KimGyuri.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/{username}")
public class UserController {
    private final UserService service;

    //사용자 정보 조회
    @GetMapping()
    public UserInfoDto userInfo(@PathVariable("username") String username) {
        return service.readUser(username);
    }

    //팔로우/팔로우해제
    @PostMapping("/follow")
    public ResponseDto follow(@PathVariable("username") String username) {
        String result = service.followUser(username);
        ResponseDto response = new ResponseDto();
        if (result.equals("follow"))
            response.setMessage(username + "님을 팔로우했습니다.");
        else
            response.setMessage(username + "님의 팔로우를 해제했습니다.");
        return response;
    }
}
