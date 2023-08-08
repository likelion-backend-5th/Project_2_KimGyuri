package com.example.Project_2_KimGyuri.exceptions;

public class ArticleCreateException extends Status400Exception{

    public ArticleCreateException() {
        super("제목과 내용을 확인해주세요.");
    }
}
