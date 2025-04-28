package com.example.project.emotionCore.controller;

import com.example.project.emotionCore.Service.GoogleService;
import com.example.project.emotionCore.dto.CodeRequestDTO;
import com.example.project.emotionCore.dto.JwtTokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/login/oauth2", produces = "application/json")
public class GoogleController {

    GoogleService googleService;

    @Autowired
    public GoogleController(GoogleService googleService) {
        this.googleService = googleService;
    }

    @PostMapping("/code/{registrationId}")
    public ResponseEntity<Void> googleLogin(@RequestBody CodeRequestDTO dto) {
        JwtTokenDTO tokens = googleService.socialLogin(dto.getCode());

        // Access Token 쿠키 생성
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", tokens.getAccessToken())
                .httpOnly(true)    // 클라이언트 JavaScript에서 접근 불가
                .secure(true)      // HTTPS 통신에서만 전송
                .path("/")         // 쿠키 경로
                .maxAge(3600)      // 1시간 유지
                .sameSite("Lax")
                .build();

        // Refresh Token 쿠키 생성
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokens.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(604800)    // 7일 유지
                .sameSite("Lax")
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .build();
    }
}
