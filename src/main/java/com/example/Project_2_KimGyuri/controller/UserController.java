package com.example.Project_2_KimGyuri.controller;

import com.example.Project_2_KimGyuri.dto.AcceptedDto;
import com.example.Project_2_KimGyuri.dto.RequestFriendDto;
import com.example.Project_2_KimGyuri.dto.ResponseDto;
import com.example.Project_2_KimGyuri.dto.UserInfoDto;
import com.example.Project_2_KimGyuri.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    //친구 요청
    @PostMapping("/add")
    public ResponseDto addFriend(@PathVariable("username") String username) {
        String result = service.requestFriend(username);
        ResponseDto response = new ResponseDto();
        if (result.equals("success"))
            response.setMessage(username + "님에게 친구 요청을 보냈습니다.");
        else
            response.setMessage("이미 친구 요청을 보냈거나 친구 관계인 사용자입니다.");
        return response;
    }

    //친구 요청 목록 조회
    @GetMapping("/request")
    public List<RequestFriendDto> requestFriend(@PathVariable("username") String username) {
        return service.checkRequest(username);
    }

    //친구 요청 수락/거절
    @PutMapping("/request/{fromUser}")
    public ResponseDto accepted(@PathVariable("username") String username, @PathVariable("fromUser") String fromUser, @RequestBody AcceptedDto dto) {
        String result = service.requestAccepted(username, fromUser, dto);
        ResponseDto response = new ResponseDto();
        if (result.equals("accept"))
            response.setMessage(username + "님의 친구 요청을 수락했습니다.");
        else
            response.setMessage(username + "님의 친구 요청을 거절했습니다.");
        return response;
    }
}
