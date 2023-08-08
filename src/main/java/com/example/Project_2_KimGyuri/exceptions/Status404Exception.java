package com.example.Project_2_KimGyuri.exceptions;

public abstract class Status404Exception extends RuntimeException {

    public Status404Exception(String message) {
        super(message);
    }
}
