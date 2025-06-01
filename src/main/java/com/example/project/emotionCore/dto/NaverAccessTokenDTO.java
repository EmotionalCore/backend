package com.example.project.emotionCore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "네이버 로그인시 토큰 넘기는 용도 DTO")
public class NaverAccessTokenDTO{
    private String access_token;

    public void setAccess_token(String access_token){
        this.access_token = access_token;
    }

    public String getAccess_token(){
        return access_token;
    }
}
