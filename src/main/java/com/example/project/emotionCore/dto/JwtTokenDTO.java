package com.example.project.emotionCore.dto;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
public class JwtTokenDTO {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}