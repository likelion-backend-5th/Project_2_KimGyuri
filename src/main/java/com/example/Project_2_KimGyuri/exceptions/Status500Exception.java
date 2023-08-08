package com.example.Project_2_KimGyuri.exceptions;

public class Status500Exception extends RuntimeException{
    public Status500Exception(String message) {
        super(message);
    }
}
