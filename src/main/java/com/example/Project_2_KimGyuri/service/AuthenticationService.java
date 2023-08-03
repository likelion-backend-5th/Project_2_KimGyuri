package com.example.Project_2_KimGyuri.service;

import com.example.Project_2_KimGyuri.entity.user.CustomUserDetails;
import com.example.Project_2_KimGyuri.entity.user.UserEntity;
import com.example.Project_2_KimGyuri.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
public class AuthenticationService implements UserDetailsManager {
    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
}
