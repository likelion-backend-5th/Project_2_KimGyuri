package com.example.Project_2_KimGyuri.exceptions;

public class AuthorizationException extends Status403Exception{

    public AuthorizationException() {
        super("권한이 없습니다.");
    }
}
