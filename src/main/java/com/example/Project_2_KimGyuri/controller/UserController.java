package com.example.Project_2_KimGyuri.controller;

import com.example.Project_2_KimGyuri.dto.UserInfoDto;
import com.example.Project_2_KimGyuri.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
