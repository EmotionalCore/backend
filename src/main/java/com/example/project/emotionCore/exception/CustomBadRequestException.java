package com.example.project.emotionCore.exception;

import lombok.Getter;

@Getter
public class CustomBadRequestException extends RuntimeException {
    int code;
    public CustomBadRequestException(int code, String message) {
        super(message);
        this.code = code;
    }
}
