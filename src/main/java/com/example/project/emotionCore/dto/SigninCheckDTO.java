package com.example.project.emotionCore.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class SigninCheckDTO {
    private int flag;
    private String sessionId;
}
