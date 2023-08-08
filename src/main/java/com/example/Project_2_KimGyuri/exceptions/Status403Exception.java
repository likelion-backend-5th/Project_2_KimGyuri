package com.example.Project_2_KimGyuri.exceptions;

public abstract class Status403Exception extends RuntimeException {
    public Status403Exception(String message) {
        super(message);
    }
}

