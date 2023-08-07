package com.example.Project_2_KimGyuri.service;

import com.example.Project_2_KimGyuri.dto.AcceptedDto;
import com.example.Project_2_KimGyuri.dto.CommentDto;
import com.example.Project_2_KimGyuri.dto.RequestFriendDto;
import com.example.Project_2_KimGyuri.dto.UserInfoDto;
import com.example.Project_2_KimGyuri.entity.UserFollowsEntity;
import com.example.Project_2_KimGyuri.entity.UserFriendsEntity;
import com.example.Project_2_KimGyuri.entity.user.UserEntity;
import com.example.Project_2_KimGyuri.jwt.JwtTokenUtils;
import com.example.Project_2_KimGyuri.repository.UserFollowRepository;
import com.example.Project_2_KimGyuri.repository.UserFriendsRepository;
import com.example.Project_2_KimGyuri.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final HttpServletRequest request;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserRepository userRepository;
    private final UserFollowRepository userFollowRepository;
    private final UserFriendsRepository userFriendsRepository;

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
            userFollows.setFollowing(user);
            userFollows.setFollower(loginUser);
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

    //친구 요청
    public String requestFriend(String username) {
        UserEntity loginUser = getUserFromToken();

        if (loginUser.getUsername().equals(username))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); //본인한테 친구 요청 불가. 권한이 없습니다.

        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //사용자를 찾을 수 없습니다.

        Optional<UserFriendsEntity> optionalUserFriends = userFriendsRepository.findByToUserIsAndFromUserIs(optionalUser.get(), loginUser);
        if (optionalUserFriends.isPresent()) {
            return "already"; //이미 친구 요청을 보냈거나 친구 관계인 사용자입니다.
        }

        UserFriendsEntity request = new UserFriendsEntity();
        request.setFromUser(loginUser);
        request.setToUser(optionalUser.get());
        request.setAccepted(false);
        userFriendsRepository.save(request);
        return "success";
    }

    //친구 요청 목록 조회
    public List<RequestFriendDto> checkRequest(String username) {
        UserEntity loginUser = getUserFromToken();

        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //사용자를 찾을 수 없습니다.
        }

        if (!loginUser.getId().equals(optionalUser.get().getId()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); //본인 친구 요청 목록이 아니라면 조회 불가

        List<UserFriendsEntity> requests = userFriendsRepository.findAllByToUserIsAndAcceptedEquals(loginUser, false);

        List<RequestFriendDto> requestDtoList = new ArrayList<>();
        for (UserFriendsEntity request : requests) {
            RequestFriendDto dto = RequestFriendDto.fromEntity(request);
            requestDtoList.add(dto);
        }

        return requestDtoList;
    }

    //친구 요청 수락/거절
    public String requestAccepted(String username, String fromUser, AcceptedDto dto) {
        UserEntity loginUser = getUserFromToken();

        Optional<UserEntity> optionalUser = userRepository.findByUsername(username); //친구 요청 받은 사용자
        Optional<UserEntity> optionalFromUser = userRepository.findByUsername(fromUser); //친구 요청 보낸 사용자
        if (optionalUser.isEmpty() || optionalFromUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //사용자를 찾을 수 없습니다.
        }

        if (!optionalUser.get().getId().equals(loginUser.getId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); //권한이 없습니다.
        }

        Optional<UserFriendsEntity> optionalUserFriends = userFriendsRepository.findByToUserIsAndAcceptedEquals(optionalUser.get(), false);
        Optional<UserFriendsEntity> optionalRequestFriends = userFriendsRepository.findByToUserIsAndFromUserIs(optionalUser.get(), optionalFromUser.get());
        if (optionalUserFriends.isEmpty() || optionalRequestFriends.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); //친구 요청을 찾을 수 없습니다.
        }

        //수락
        if (dto.isAccepted()) {
            Optional<UserFriendsEntity> toUser = userFriendsRepository.findByToUserIsAndAcceptedEquals(loginUser, false);
            if (toUser.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND); //이미 처리된 요청입니다.
            }
            toUser.get().setAccepted(true);
            UserFriendsEntity friend = new UserFriendsEntity();
            friend.setFromUser(loginUser);
            friend.setToUser(optionalFromUser.get());
            friend.setAccepted(true);
            userFriendsRepository.save(friend);
            return "accept";
        }
        //거절
        else {
            Optional<UserFriendsEntity> toUser = userFriendsRepository.findByToUserIsAndAcceptedEquals(loginUser, false);
            if (toUser.isEmpty())
                throw new ResponseStatusException(HttpStatus.NOT_FOUND); //이미 처리된 요청입니다.
            userFriendsRepository.deleteById(toUser.get().getId());
            return "reject";
        }
    }
}
