package com.example.Project_2_KimGyuri.exceptions;

public class ArticleNotFoundException extends Status404Exception{
    public ArticleNotFoundException() {
        super("해당 피드를 찾을 수 없습니다.");
    }
}
