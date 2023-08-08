package com.example.Project_2_KimGyuri.exceptions;

public class RequestFriendException extends Status404Exception{
    public RequestFriendException() {
        super("해당 친구 요청을 찾을 수 없습니다.");
    }
}
